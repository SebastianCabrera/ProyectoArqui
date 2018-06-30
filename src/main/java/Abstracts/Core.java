package Abstracts;

import Instructions.Instructions;
import Structures.*;

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
    protected CyclicBarrier barrier;                // Referencia a la barrera que sincroniza a los núcleos.

    // Variables propias

    protected Vector<Registers> contextsList;       // Lista de contextos pertenecientes al núcleo.
    protected DataCache dataCache;                  // Caché de datos propia del núcleo.
    protected InstructionCache instructionCache;    // Caché de instrucciones propia del núcleo.
    protected Registers registers;                  // Registros asociados al núcleo
    protected int coreId;                           // Identificación del núcleo actual.

    // Variables de utilidades

    protected Core otherCoreReference;              // Referencia al otro núcleo para poder acceder a sus cachés (Snooping).
    protected Instructions instructions;            // Instancia de la clase que ejecutará cada instrucción leída por el núcleo.
    protected Vector<Integer> filesBeginDirection;  // Referencia a la posición de memoria en la cual inicia cada hilillo (file) cargado a memoria de instrucciones.
    protected Vector<Boolean> takenFiles;           // Referencia a un vector que indica, para cada hilillo, cuál núcleo lo tomó.
    protected Semaphore semaphore;                  // Semáforo para modificar la lista de hilillos tomados de forma atómica.
    protected Vector<Integer> myTakenFiles;         // Vector que guarda el ID de cada hilillo tomado por el núcleo actual.
    protected Vector<Registers> results;            // Referencia al vector de resultados finales de cada hilillo.

    /**
     * Constructor abstracto de los núcleos.
     * @param cacheSize El tamaño de las cachés (posiciones), dependiendo del núcleo.
     * @param insMem La referencia a la memoria de instrucciones del programa.
     * @param dataMem La referencia a la memotia de datos del programa.
     * @param programBarrier La referencia a la barrera que sincroniza los núcleos.
     * @param fbd La referencia al vector que indica la posición de inicio de cada hilillo.
     * @param tf La referencia al vector que indica cuáles hilillos ya fueron o no tomados por algún núcleo.
     * @param s El semáforo qque controlará el acceso al vector de hilillos tomados.
     * @param res La refencia al vector de resultados de cada hilillo para actualizar.
     */
    protected Core(int cacheSize, InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> fbd, Vector<Boolean> tf, Semaphore s, Vector<Registers> res){
        this.dataCache = new DataCache(cacheSize);
        this.instructionCache = new InstructionCache(cacheSize);

        this.instructionMemory = insMem;
        this.dataMemory = dataMem;

        this.barrier = programBarrier;
        this.filesBeginDirection = fbd;
        this.takenFiles = tf;
        this.semaphore = s;
        this.results = res;

        this.registers = new Registers();
        this.contextsList = new Vector<>();
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
    public Vector<Registers> getContextsList(){
        return this.contextsList;
    }

    /**
     * Obtiene la lista de IDs de hilillos que fueron tomados por el núcleo
     * @return la lista IDs de hilillos
     */
    public Vector<Integer> getMyTakenFiles(){
        return this.myTakenFiles;
    }
}