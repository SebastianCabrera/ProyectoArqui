package Cores;

import Enums.Codes;
import Instructions.Instructions;
import Structures.*;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clase que representa a un núcleo con capacidad para un solo hilillo. Este ejecutará las instrucciones.
 */
public class SingleCore extends Thread {

    // Variables compartidas

    private DataMemory dataMemory;                // Referencia a la memoria de datos compartida.
    private InstructionMemory instructionMemory;  // Referencia a la memoria de instrucciones compartida.
    private int clock;                            // Reloj del núcleo. Todos los núcleos deben mantenerlo igual en cada iteración.
    private int userSlowModeCycles;               // Cantidad de ciclos para activar el modo lento.
    private CyclicBarrier generalBarrier;         // Referencia a la barrera que sincroniza a las impresiones.
    private CyclicBarrier cycleBarrier;           // Referencia a la barrera que sincroniza a los núcleos.
    private CyclicBarrier slowModeBarrier;        // Referencia a la barrera que controla el modo lento.
    private Queue<Registers> contextsList;        // Referencia a la cola de contextos del programa.
    private Queue<Integer> contextsListID;        // Referencia a la cola que indica el ID del hilillo que esta en la cola de contextos en cierta posicion.

    // Variables propias

    private DataCache dataCache;                  // Caché de datos propia del núcleo.
    private InstructionCache instructionCache;    // Caché de instrucciones propia del núcleo.
    private Registers registers;                  // Registros asociados al núcleo
    private int coreId;                           // Identificación del núcleo actual.
    private int userQuantum;                      // El quantum que define el usuario para los nucleos
    private boolean finished;                     // Indica si el nucleo no tiene mas hilillos por ejecutar
    private int currentQuantum;                   // El quantum que le sobra al núcleo actualmente

    // Variables de utilidades

    private SingleCore otherCoreReference;        // Referencia al otro núcleo para poder acceder a sus cachés (Snooping).
    private Instructions instructions;            // Instancia de la clase que ejecutará cada instrucción leída por el núcleo.
    private Vector<Integer> filesBeginDirection;  // Referencia a la posición de memoria en la cual inicia cada hilillo (file) cargado a memoria de instrucciones.
    private Vector<Integer> takenFiles;           // Referencia a un vector que indica, para cada hilillo, cuál núcleo lo tomó. (0=no tomado, 1=detenido, 2=terminado)
    private Semaphore filesStatusSemaphore;       // Semáforo para modificar la lista de hilillos tomados de forma atómica.
    private Vector<Integer> myTakenFiles;         // Vector que guarda el ID de cada hilillo tomado por el núcleo actual.
    private Vector<Registers> results;            // Referencia al vector de resultados finales de cada hilillo.
    private String currentRunningFile;            // Nombre del hilillo ejecutándose actualmente
    private boolean executeCore;                  // Indica si core continúa ejecutando un hilillo o si debe parar (instrucción FIN encontrada o fin de quantum).

    /**
     * Constructor de los núcleos. Utiliza todas las referencias y otras variables necesarias para
     * su funcionamiento
     * @param insMem La referencia a la memoria de instrucciones del programa.
     * @param dataMem La referencia a la memotia de datos del programa.
     * @param programBarrier La referencia a la barrera que sincroniza los núcleos.
     * @param beginDirections La referencia al vector que indica la posición de inicio de cada hilillo.
     * @param taken La referencia al vector que indica cuáles hilillos ya fueron o no tomados por algún núcleo.
     * @param semaphore El semáforo qque controlará el acceso al vector de hilillos tomados.
     * @param resultsRegisters La refencia al vector de resultados de cada hilillo para actualizar.
     * @param contexts La referencia a la cola de contextos del programa.
     * @param contextsID La referencia a los ID de cada hilillo según su posición.
     * @param cycleBarrier La referencia a la barrera que controla los ciclos de cada núcleo.
     * @param quantum El quantum que se le dará a cada núcleo.
     * @param id El identificador del núcleo.
     * @param slowCycles Cantidad de ciclos que el usuario definió para activar el modo lento.
     * @param slowBarrier La referencia a la barrera que controla el modo lento.
     */
    public SingleCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier,
                   Vector<Integer> beginDirections, Vector<Integer> taken, Semaphore semaphore, Vector<Registers> resultsRegisters, Queue<Registers> contexts,
                   Queue<Integer> contextsID, CyclicBarrier cycleBarrier, int quantum, int id, int slowCycles, CyclicBarrier slowBarrier){

        this.coreId = id;

        int cacheSize;

        // El tamaño de la caché depende depende de cuál es el core creado.
        if(id == Codes.CORE_0) {
            cacheSize = Codes.BLOCKS_IN_CACHE_0;
        }else{
            cacheSize = Codes.BLOCKS_IN_CACHE_1;
        }

        this.dataCache = new DataCache(cacheSize);
        this.instructionCache = new InstructionCache(cacheSize);

        this.instructionMemory = insMem;
        this.dataMemory = dataMem;

        this.generalBarrier = programBarrier;
        this.cycleBarrier = cycleBarrier;
        this.slowModeBarrier = slowBarrier;
        this.filesBeginDirection = beginDirections;
        this.takenFiles = taken;
        this.filesStatusSemaphore = semaphore;
        this.results = resultsRegisters;
        this.userQuantum = quantum;
        this.currentQuantum = quantum;
        this.finished = false;

        this.registers = new Registers();
        this.contextsList = contexts;
        this.contextsListID = contextsID;
        this.clock = 0;
        this.userSlowModeCycles = slowCycles;
        this.instructions = new Instructions();
        this.myTakenFiles = new Vector<>();
        this.currentRunningFile = "";
        this.executeCore = true;
    }

    @Override
    /**
     * Método que ejecuta cada thread. En este caso permite al núcleo ejecutar cada instrucción encontrada.
     */
    public void run(){
        int block;      // Bloque de memoria de instrucciones
        int word;       // Palabra de memoria de instrucciones
        int direction;  // Dirección de memoria de instrucciones
        int position;   // Posición de caché de instrucciones

        // Ejecuta el cilco principal de obtención de contextos disponibles mientras siga habiendo contextos
        while(true) {

            try {
                // Adquiere el semáforo para poder revisar la cola de contextos de forma atómica y evitar
                // que el otro núcleo cambie el contenido de la cola mientras este sigue necesitando el contenido.
                this.filesStatusSemaphore.acquire();

                // Mientras haya contextos.
                if(!contextsList.isEmpty()) {

                    // Se copia el primer contexto a los registros del núcleo.
                    this.registers = new Registers(contextsList.poll());

                    // Se identifica a cuál hilillo corresponde el contexto.
                    int fileID = contextsListID.poll();
                    this.currentRunningFile = fileID + ".txt";

                    // Se agrega el hilillo actual como tomado por el núcleo actual.
                    this.myTakenFiles.add(fileID);

                    // Si el hilillo actual no había sido tomado nunca, significa que su ejecución no había comenzado jamás,
                    // por lo cual se coloca el PC en la dirección inicial del hilillo. Sino, se usa el último PC guardado.
                    if (this.takenFiles.get(fileID) == Codes.NOT_TAKEN) {
                        this.registers.setRegister(Codes.PC, this.filesBeginDirection.get(fileID) - Codes.INSTRUCTION_MEM_BEGIN);
                    }

                    // Libera el semáforo ya que no necesita revisar más.
                    this.filesStatusSemaphore.release();

                    executeCore = true;

                    boolean instructionsFailure = false;

                    // Se ejecuta el núcleo para ejecutar instrucciones.
                    while (executeCore) {

                        this.tryToWaitSlowMode();

                        // Si el quantum llega a 0, se guarda el contexto del hilillo para obtener otro.
                        if (currentQuantum == 0) {
                            try{
                                // Se toma el semáforo para volver a modificar la cola y estados.
                                filesStatusSemaphore.acquire();

                                currentQuantum = userQuantum;

                                // Se agregan los registros al final de la cola de contextos junto con su ID
                                this.contextsList.add(new Registers(this.registers));
                                this.contextsListID.add(fileID);

                                // El hilillo tiene el estado "en progreso" para evitar que la próxima vez que sea tomado
                                // se comienze desde la primera dirección de memoria del mismo.
                                this.takenFiles.set(fileID, Codes.IN_PROGRESS);

                                filesStatusSemaphore.release();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }

                            // Se acaba la ejecución del hilillo.
                            executeCore = false;

                        // En caso de que el quantum no se haya acabado.
                        } else {

                            // Se calcula la dirección, bloque, palabra de instrucciones y la posición de caché.
                            direction = (this.registers.getRegister(Codes.PC) + Codes.INSTRUCTION_MEM_BEGIN);
                            block = this.calculateInstructionBlock(this.registers.getRegister(Codes.PC));
                            word = this.calculateWord(direction);
                            position = (block % Codes.DATA_WORD_BYTES);

                            // Si la etiqueta del bloque de instrucciones no es igual al bloque calculado, es un fallo
                            // de caché
                            if (this.instructionCache.getTag(position) != block) {

                                // Si no se consigue el bus, se intenta hasta el próximo ciclo.
                                if(!(this.instructionMemory.getMemoryBusLock()).tryLock()){
                                    instructionsFailure = true;
                                }else {

                                    // Se carga el bloque en caché y se libera el bus.
                                    this.instructionCache.setBlock(position, this.instructionMemory.getBlock(this.getBlockBegin(this.getRealInstructionDirection(direction))));
                                    this.instructionCache.setTag(position, block);

                                    this.instructionMemory.getMemoryBusLock().unlock();
                                }
                            }

                            // En caso de no haber resuelto el fallo
                            if(!instructionsFailure) {

                                // Instrucción
                                Vector<Integer> instruction = this.instructionCache.getWord(position, word);

                                //Se ejecuta la instruccion del hilillo
                                this.instructions.decode(this.registers, instruction, this.dataMemory, this, this.cycleBarrier);

                                //Pasa un ciclo, se toma en cuenta para el quantum
                                this.currentQuantum--;
                            }else{
                                instructionsFailure = false;
                            }
                        }

                        // Espera a que el otro núcleo también ejecute el ciclo para avanzar el reloj a la vez.
                        try {
                            System.err.println("CORE " + this.coreId + ": BARRIER INSTRUCTION");
                            cycleBarrier.await();
                            this.clock++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }

                    // Cuando se acaba la lectura del hilillo, se almacenan sus resultados
                    this.results.set(fileID, this.registers);

                }else{
                    this.filesStatusSemaphore.release();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.finished = true;

        // Si el otro núcleo no ha terminado, este espera para que ambos terminen su ejecución total a la vez y además
        // avanza el reloj por cada instrucción ejecutada por el otro núcleo.
        do {
            try {
                System.err.println("CORE " + this.coreId + ": BARRIER FINISH");
                cycleBarrier.await();
                this.clock++;

                this.tryToWaitSlowMode();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println("Barrera reiniciada para que los cores terminen.");
            }
        }while (!this.otherCoreReference.getFinishedState());

        try {
            // Debido una posible desincronización al final de la ejecución, se desactiva la barrera, pues para este punto
            // ya ambos núcleos debieron haber terminado.
            this.cycleBarrier.reset();
            System.err.println("CORE " + this.coreId + ": BARRIER GENERAL");

            // Espera a que el otro núcleo llegue a esta barrera para acabar el programa juntos y que el hilo principal
            // pueda imprimir resultados.
            this.generalBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    // Metodos modificadores

    /**
     * Modifica la referencia al otro núcleo que posee el núcleo actual
     * @param reference El otro núcleo a colocar como referencia.
     */
    public void setOtherCoreReference(SingleCore reference){
        this.otherCoreReference = reference;
    }

    /**
     * Agrega un ciclo al reloj del núcleo
     */
    public void addToClock(){
        this.clock++;
    }

    /**
     * Cambia el estado de ejecución del núcleo para que se detenga
     */
    public void stopExecution(){
        this.executeCore = false;
    }

    // Métodos obtenedores

    /**
     * Obtiene el reloj actual del núcleo
     * @return El reloj del núcleo.
     */
    public int getClock(){
        return this.clock;
    }

    /**
     * Obtiene una referencia a la caché de datos del núcleo.
     * @return La caché de datos del núcleo.
     */
    public DataCache getDataCache(){
        return this.dataCache;
    }

    /**
     * Obtiene el ID del núcleo actual.
     * @return El ID del núcleo.
     */
    public int getCoreId(){
        return this.coreId;
    }

    /**
     * Obtiene la referencia al otro núcleo que posee el núcleo actual.
     * @return La referencia al otro núcleo.
     */
    public SingleCore getOtherCoreReference(){
        return this.otherCoreReference;
    }

    /**
     * Obtiene el estado de finalización del núcleo
     * @return El estado de finalización
     */
    public boolean getFinishedState(){
        return this.finished;
    }

    // Métodos asociados a cálculos

    /**
     * Calcula el número de bloque al cual pertenece una dirección de la memoria de datos.
     * @param memDirection Dirección de memoria inicial del bloque.
     * @return La posición en la cual debería ir el bloque.
     */
    public int calculateDataBlockNumber(int memDirection){
        return memDirection / Codes.DATA_BLOCK_BYTES;
    }

    /**
     * Calcula una palabra específica dentro de un bloque de datos, basada en la dirección de memoria.
     * @param memDirection Dirección de memoria en la cual se encuentra la palabra
     * @return El número de palabra dentro del bloque
     */
    public int calculateDataWordPosition(int memDirection){
        return (memDirection % Codes.DATA_BLOCK_BYTES) / Codes.DATA_WORD_BYTES;
    }

    /**
     * Calcula la posición de la caché de datos en la cual debería estar un bloque.
     * @param blockNumber El número de bloque del cual se quiere obtener su posición en caché.
     * @param coreId El ID del núcleo para saber cuántas posiciones tiene la caché.
     * @return La posición en caché en la cual se debería guardar el bloque.
     */
    public int calculateCachePosition(int blockNumber, int coreId){
        switch (coreId){
            case Codes.CORE_0:
                return (blockNumber % Codes.BLOCKS_IN_CACHE_0);
            default:
                return (blockNumber % Codes.BLOCKS_IN_CACHE_1);
        }
    }

    /**
     * Calcula y obtiene la dirección inicial de un bloque en cualquiera de las 2 memorias con base en una dirección dentro
     * de ese bloque
     * @param direction La dirección perteneciente a un bloque
     * @return La dirección inicial del bloque
     */
    public int getBlockBegin(int direction){
        if(direction % Codes.BLOCK_BYTES != 0){
            return direction - ((direction % Codes.BLOCK_BYTES));
        }else{
            return direction;
        }
    }

    /**
     * Obtiene una posición del vector que representa a la memoria de datos, esto porque las direcciones van de 4 en 4,
     * pero el vector es continuo.
     * @param direction Dirección de memoria real
     * @return Posición en el vector de la memoria de datos
     */
    public int getMemoryDirectionPosition(int direction){
        return direction / Codes.DATA_WORD_BYTES;
    }

    /**
     * Obtiene una dirección de memoria a la cual se le resta la posición inicial de la memoria de instrucciones. Esto
     * porque la memoria de instrucciones comienza en la dirección 384, pero el vector que la represente inicia en 0.
     * @param virtualDirection Dirección virtual de la memoria.
     * @return La posición real en la memoria de instrucciones.
     */
    protected int getRealInstructionDirection(int virtualDirection){
        return virtualDirection - Codes.INSTRUCTION_MEM_BEGIN;
    }

    /**
     * Calcula un número de bloque de instrucciones basado en su dirección de memoria
     * @param direction La dirección del bloque
     * @return El número de bloque
     */
    private int calculateInstructionBlock(int direction){
        return direction / Codes.BLOCK_BYTES;
    }

    /**
     * Calcula un número de palabra de instrucciones basada en su dirección de memoria
     * @param direction La dirección del bloque
     * @return El número de palabra
     */
    private int calculateWord(int direction){
        return (direction % Codes.BLOCK_BYTES) / Codes.INSTRUCTIONS_WORD_SIZE;
    }

    // Métodos acociados a impresiones

    /**
     * Obtiene el nombre del hilillo que está ejecutándose actualmente
     * @return El nombre del hilillo
     */
    public String getCurrentRunningFile(){
        return this.currentRunningFile;
    }

    /**
     * Intenta esperar en caso de que las condiciones se cumplan, para el modo lento de impresión de datos.
     */
    public void tryToWaitSlowMode(){
        if((this.userSlowModeCycles != 0) && ((this.clock % this.userSlowModeCycles) == 0)){
            try {
                this.slowModeBarrier.await();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
