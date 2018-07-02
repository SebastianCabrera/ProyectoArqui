package Abstracts;

import Enums.Codes;
import Instructions.Instructions;
import Structures.*;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clase abstracta que representa a un núcleo de procesador. Posee métodos y atributos que ambos tipos de
 * núcleo utilizan.
 */
public abstract class Core extends Thread{

    // Variables compartidas

    protected DataMemory dataMemory;                // Referencia a la memoria de datos compartida.
    protected InstructionMemory instructionMemory;  // Referencia a la memoria de instrucciones compartida.
    protected int clock;                            // Reloj del núcleo. Todos los núcleos deben mantenerlo igual en cada iteración.
    protected CyclicBarrier generalBarrier;         // Referencia a la barrera que sincroniza a las impresiones.
    protected CyclicBarrier cycleBarrier;           // Referencia a la barrera que sincroniza a los núcleos.
    protected Queue<Registers> contextsList;        // Referencia a la cola de contextos del programa.
    protected Queue<Integer> contextsListID;        // Referencia a la cola que indica el ID del hilillo que esta en la cola de contextos en cierta posicion.

    // Variables propias

    protected DataCache dataCache;                  // Caché de datos propia del núcleo.
    protected InstructionCache instructionCache;    // Caché de instrucciones propia del núcleo.
    protected Registers registers;                  // Registros asociados al núcleo
    protected int coreId;                           // Identificación del núcleo actual.
    protected int quantum;                          // El quantum que define el usuario para los nucleos
    protected boolean finished;                     // Indica si el nucleo no tiene mas hilillos por ejecutar
    protected boolean semaphoreState;               // Auxilar que permite saber el estado del semaforo

    // Variables de utilidades

    protected Core otherCoreReference;              // Referencia al otro núcleo para poder acceder a sus cachés (Snooping).
    protected Instructions instructions;            // Instancia de la clase que ejecutará cada instrucción leída por el núcleo.
    protected Vector<Integer> filesBeginDirection;  // Referencia a la posición de memoria en la cual inicia cada hilillo (file) cargado a memoria de instrucciones.
    protected Vector<Integer> takenFiles;           // Referencia a un vector que indica, para cada hilillo, cuál núcleo lo tomó. (0=no tomado, 1=detenido, 2=terminado)
    protected Semaphore semaphore;                  // Semáforo para modificar la lista de hilillos tomados de forma atómica.
    protected Vector<Integer> myTakenFiles;         // Vector que guarda el ID de cada hilillo tomado por el núcleo actual.
    protected Vector<Registers> results;            // Referencia al vector de resultados finales de cada hilillo.

    /**
     * Constructor abstracto de los núcleos.
     * @param insMem La referencia a la memoria de instrucciones del programa.
     * @param dataMem La referencia a la memotia de datos del programa.
     * @param programBarrier La referencia a la barrera que sincroniza los núcleos.
     * @param beginDirections La referencia al vector que indica la posición de inicio de cada hilillo.
     * @param taken La referencia al vector que indica cuáles hilillos ya fueron o no tomados por algún núcleo.
     * @param sem El semáforo qque controlará el acceso al vector de hilillos tomados.
     * @param resultsRegisters La refencia al vector de resultados de cada hilillo para actualizar.
     * @param contexts La referencia a la cola de contextos del programa.
     * @param contextsID La referencia a los ID de cada hilillo según su posición.
     * @param cycleBarrier La referencia a la barrera que controla los ciclos de cada núcleo.
     * @param quantum El quantum que se le dará a cada núcleo.
     * @param id El identificador del núcleo.
     */
    protected Core(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier,
                   Vector<Integer> beginDirections, Vector<Integer> taken, Semaphore sem, Vector<Registers> resultsRegisters, Queue<Registers> contexts,
                   Queue<Integer> contextsID, CyclicBarrier cycleBarrier, int quantum, int id){
        
        this.coreId = id;

        int cacheSize;

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
        this.filesBeginDirection = beginDirections;
        this.takenFiles = taken;
        this.semaphore = sem;
        this.results = resultsRegisters;
        this.quantum = quantum;
        this.finished = false;
        this.semaphoreState = false;

        this.registers = new Registers(); //TODO por ahora
        this.contextsList = contexts;
        this.contextsListID = contextsID;
        this.clock = 0;
        this.instructions = new Instructions();
        this.myTakenFiles = new Vector<>();
    }

    // Metodos modificadores

    /**
     * Modifica la referencia al otro núcleo que posee el núcleo actual
     * @param reference El otro núcleo a colocar como referencia.
     */
    public void setOtherCoreReference(Core reference){
        this.otherCoreReference = reference;
    }

    // TODO este método no debería obtener registros como parámetros, se supone que voy a guardar mis propios registros.
    /**
     * Agrega los registros actuales a la lista de contextos
     * @param registers // TODO
     */
    public void addContext(Registers registers){
        this.contextsList.add(registers);
    }

    /**
     * Agrega un ciclo al reloj del núcleo
     */
    public void addToClock(){
        this.clock++;
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
     * Obtiene una referencia a la caché de instrucciones del núcleo.
     * @return La caché de instrucciones del núcleo.
     */
    public InstructionCache getInstructionCache(){
        return this.instructionCache;
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
    public Core getOtherCoreReference(){
        return this.otherCoreReference;
    }

    /**
     * Obtiene una referencia a los registros del núcleo.
     * @return Los registros del núcleo.
     */
    public Registers getRegisters(){
        return this.registers;
    }

    /**
     * Obtiene la lista con todos los contextos guardados del núcleo
     * @return la lista de contextos del núcleo.
     */
    public Queue<Registers> getContextsList(){
        return this.contextsList;
    }

    /**
     * Obtiene la lista de IDs de hilillos que fueron tomados por el núcleo
     * @return la lista IDs de hilillos
     */
    public Vector<Integer> getMyTakenFiles(){
        return this.myTakenFiles;
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
     * Calcula
     * @param direction
     * @return
     */
    public int getBlockBegin(int direction){
        if(direction % Codes.BLOCK_BYTES != 0){
            return direction - ((direction % Codes.BLOCK_BYTES));
        }else{
            return direction;
        }
    }

    public int getMemoryDirectionPosition(int direction){
        return direction / Codes.DATA_WORD_BYTES;
    }

    protected int getRealInstructionDirection(int virtualDirection){
        return virtualDirection - Codes.INSTRUCTION_MEM_BEGIN;
    }
}