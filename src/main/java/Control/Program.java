package Control;

import Cores.SingleCore;
import Enums.Codes;
import GraphicInterface.ResultsWindow;
import GraphicInterface.SlowModeWindow;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clase que funciona como programa principal para ejecutar la simulación.
 */
public class Program extends Thread {
    private SingleCore core0;                       // Núcleo 0
    private SingleCore core1;                       // Núcleo 1
    private InstructionMemory instructionMemory;    // Memoria de instrucciones del sistema
    private DataMemory dataMemory;                  // Memoria de datos del sistema
    private int clock;                              // Reloj del sistema
    private final CyclicBarrier generalBarrier;     // Barrera general para controlar cuando terminan los cores e imprimir
    private final CyclicBarrier cycleBarrier;       // Barrera que controla los ciclos de reloj
    private final CyclicBarrier slowModeBarrier;    // Barrera que se utilizará para controlar el modo lento.
    private Vector<Integer> filesBeginDirection;    // La dirección en la cual inicia cada uno de los hilillos en memoria
    private Vector<Integer> takenFiles;             // Identifica cuáles hilillos ya fueron tomados por un thread
    private Queue<Registers> contextsList;          // Cola de contextos para almacenar en caso de fin de quantum
    private Queue<Integer> contextsListID;          // Cola que identifica cuál hilillo está en cuál posición de la cola
    private Semaphore updateFilesSemaphore;         // Semáforo para que se modifiquen los vectores anteriores de forma atómica
    private Vector<Registers> results;              // Vector que almacena los resultados de cada hilillo para imprimir
    private int quantum;                            // Quantum del sistema
    private int userSlowModeCycles;                 // Ciclos para activar el modo lento

    private SlowModeWindow slowModeWindow;          // Referencia a la ventana del modo lento.
    private ResultsWindow resultsWindow;            // Ventana para imprimir los resultados

    /**
     * Constructor del programa.
     * @param userQuantum El quantum para los núcleos, dado por el usuario.
     * @param slowModeCycles Cantidad de ciclos que el usuario definió para activar el modo lento.
     * @param slowModeBarrier La referencia a la barrera que controla el modo lento.
     */
    public Program(int userQuantum, int slowModeCycles, CyclicBarrier slowModeBarrier){
        this.instructionMemory = new InstructionMemory();
        this.dataMemory = new DataMemory();

        this.userSlowModeCycles = slowModeCycles;
        this.clock = 0;
        this.quantum = userQuantum;

        // La barrera general controla a los 2 núcleos y al hilo principal
        this.generalBarrier = new CyclicBarrier(3);

        // La barrera de ciclos controla a los 2 núcleos
        this.cycleBarrier = new CyclicBarrier(2);

        this.slowModeBarrier = slowModeBarrier;

        this.filesBeginDirection = new Vector<>();

        // Se agrega la primera posición del primer hilillo
        this.filesBeginDirection.add(Codes.INSTRUCTION_MEM_BEGIN);

        this.takenFiles = new Vector<>();
        this.contextsList = new LinkedList<>();
        this.contextsListID = new LinkedList<>();
        this.results = new Vector<>();

        // Tiene un solo permiso porque solo puede usarlo un hilo a la vez
        this.updateFilesSemaphore = new Semaphore(1);

        this.resultsWindow = new ResultsWindow(this);

        // Para que la ventana aparezca en el centro
        resultsWindow.setLocationRelativeTo(null);
    }

    /**
     * Permite cargar los hilillos en la memoria de instrucciones
     * @param files Cada uno de los hilillos cargados
     */
    public void loadInstructions(String[] files){
        int memoryPosition = 0;

        // Carga cada hilillo en un ciclo
        for(int i = 0; i < files.length; i++){
            File encyptedFile = new File(".\\Hilillos\\" + files[i]);

            // Para cada hilillo, se agrega como no tomado por ningún núcleo como estado inicial
            this.takenFiles.add(Codes.NOT_TAKEN);

            // Se inician los resutlados y contextos como registros vacíos.
            this.results.add(new Registers());
            this.contextsList.add(new Registers());

            // Se asume que los hilillos siempre tienen como primer caracter un número que indica cuál es.
            // 48 es el ASCII de 0.
            this.contextsListID.add(files[i].charAt(0) - 48);

            if (encyptedFile.isFile()) {
                String line;
                BufferedReader reader;

                try {
                    reader = new BufferedReader(new FileReader(encyptedFile));

                    try {
                        line = reader.readLine();

                        while (line != null) {

                            String[] codedLine = line.split(" ");
                            Vector<Integer> decodedLine = new Vector<>(Codes.TOTAL_INSTRUCTION_PARAMETERS);

                            for(int j = 0; j < Codes.TOTAL_INSTRUCTION_PARAMETERS; j++){
                                decodedLine.add(Integer.parseInt(codedLine[j]));
                            }

                            // Escribe en cada una de las palabras de la memoria de instrucciones.
                            this.instructionMemory.setWordData(decodedLine, memoryPosition);

                            line = reader.readLine();
                            memoryPosition += Codes.INSTRUCTIONS_WORD_SIZE;
                        }
                    } catch (IOException l) {
                        l.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // Se agrega la posición de la memoria en la cual inicia cada hilillo
            this.filesBeginDirection.add(memoryPosition + Codes.INSTRUCTION_MEM_BEGIN);
        }

        // Al final del ciclo, se carga el combobox de la ventana que permite escoger un hilillo para ver sus registros
        this.resultsWindow.fillFilesCombobox(files);
    }

    /**
     * Corre el programa principal
     */
    @Override
    public void run(){

        // Creación de los núcleos, con todas las referencias necesarias a estructuras y demás para su correcto funcionamiento.
        core0 = new SingleCore( this.instructionMemory, this.dataMemory, this.generalBarrier, this.filesBeginDirection,
                                this.takenFiles, this.updateFilesSemaphore, this.results, this.contextsList,
                                this.contextsListID, this.cycleBarrier, this.quantum, Codes.CORE_0, this.userSlowModeCycles, this.slowModeBarrier);
        core1 = new SingleCore( this.instructionMemory, this.dataMemory, this.generalBarrier, this.filesBeginDirection,
                                this.takenFiles, this.updateFilesSemaphore, this.results, this.contextsList,
                                this.contextsListID, this.cycleBarrier, this.quantum, Codes.CORE_1, this.userSlowModeCycles, this.slowModeBarrier);

        // Se coloca una referencia al otro núcleo dentro de cada núcleo para permitir el Snooping de cachés.
        core0.setOtherCoreReference(core1);
        core1.setOtherCoreReference(core0);

        // Se crean e inician los threads, uno para ejecutar cada núcleo
        Thread thread0 = new Thread(core0, Codes.THREAD_1);
        Thread thread1 = new Thread(core1, Codes.THREAD_2);
        thread0.start();
        thread1.start();

        // El programa principal espera a que los dos núcleos terminen para imprimir resultados.
        try {
            System.err.println("BARRIER GENERAL (PROGRAM)");
            generalBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        if(slowModeWindow != null) {

            this.slowModeWindow.setFinishAvailable();

            try {
                System.err.println("BARRIER SLOW (PROGRAM)");
                this.slowModeBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println("Barrera reiniciada para continuar ejecución.");
            }
        }
        // Aparece la ventana de resultados.
        resultsWindow.setVisible(true);

        // TODO Borrar impresiones debbug

        System.out.println("Clock0: " + core0.getClock());
        System.out.println("Clock1: " + core1.getClock());

        // TODO fin del ToDo

        // Se asegura que los relojes coinciden
        if((this.core0.getClock() - this.core1.getClock()) != 0){
            this.clock = Codes.FAILURE;
        }else{
            this.clock = this.core0.getClock();
        }

        // Coloca la cantidad de cliclos en la ventana
        resultsWindow.setTotalCyclesValue(this.clock);

        // Llena ambas cachés en la ventada
        resultsWindow.fillCacheTable(this.core0.getDataCache().getCache(), this.core0.getDataCache().getTags(), this.core0.getDataCache().getStates(), this.core0.getCoreId());
        resultsWindow.fillCacheTable(this.core1.getDataCache().getCache(), this.core1.getDataCache().getTags(), this.core1.getDataCache().getStates(), this.core1.getCoreId());

        // Llena la memoria de datos en la ventana
        resultsWindow.fillDataTable(this.dataMemory.getMemory());
    }

    /**
     * Obtiene los resultados de un hilillo específico basado en el nombre del mismo para imprimirlo en la ventana de
     * resultados
     * @param fileID El nombre del hilillo
     * @return Los resultados (registros) del hilillo.
     */
    public Vector<Integer> getRegistersByFileID(int fileID){
        return this.results.get(fileID).getRegisters();
    }

    /**
     * Obtiene el nombre del hilillo que está ejecutándose actualmente en un núcleo
     * @param coreID El núcleo sobre el cual se desea saber cuál hilillo ejecuta
     * @return El hilillo en ejecución
     */
    public String getCurrentRunningFile(int coreID){
        switch (coreID){
            case Codes.CORE_0:
                return this.core0.getCurrentRunningFile();
            default:
                return this.core1.getCurrentRunningFile();
        }
    }

    /**
     * Obtiene el reloj actual
     * @return El reloj general de los núcleos
     */
    public int getCurrentClock(){
        if((this.core0.getClock() - this.core1.getClock()) != 0){
            return this.clock = Codes.FAILURE;
        }else{
            return this.clock = this.core0.getClock();
        }
    }

    /**
     * Le asigna una referencia de la ventana del modo lento al núcleo en caso de haber sido activado
     * @param window La ventana de modo lento
     */
    public void setSlowModeWindow(SlowModeWindow window){
        this.slowModeWindow = window;
    }
}
