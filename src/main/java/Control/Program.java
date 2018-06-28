package Control;

import Abstracts.Core;
import Cores.DualCore;
import Cores.SingleCore;
import Enums.Codes;
import GraphicInterface.ResultsWindow;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Program {
    private Core core0;
    private Core core1;
    private InstructionMemory instructionMemory;
    private DataMemory dataMemory;
    private int clock;
    private final CyclicBarrier barrier;
    private Vector<Integer> filesBeginDirection;
    private Vector<Boolean> takenFiles;
    private Semaphore updateFileState;
    private Vector<Registers> results;

    private ResultsWindow rw;

    public Program(){
        this.instructionMemory = new InstructionMemory();
        this.dataMemory = new DataMemory();
        this.clock = 0;
        this.barrier = new CyclicBarrier(3);
        this.filesBeginDirection = new Vector<>();
        this.filesBeginDirection.add(Codes.INSTRUCTION_MEM_BEGIN);

        this.takenFiles = new Vector<>();

        this.updateFileState = new Semaphore(1);

        this.results = new Vector<>();

        this.rw = new ResultsWindow(this);
        rw.setLocationRelativeTo(null);
    }

    public void loadInstructions(String[] files){
        int memoryPosition = 0;

        for(int i = 0; i < files.length; i++){
            File encyptedFile = new File(".\\Hilillos\\" + files[i]);

            this.takenFiles.add(false);
            this.results.add(new Registers());

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

            this.filesBeginDirection.add(memoryPosition + 384);
        }

        this.rw.fillFilesCombobox(files);
    }

    public void runProgram(){


        core0 = new SingleCore(this.instructionMemory, this.dataMemory, this.barrier, this.filesBeginDirection, this.takenFiles, this.updateFileState, this.results);
        core1 = new SingleCore(this.instructionMemory, this.dataMemory, this.barrier, this.filesBeginDirection, this.takenFiles, this.updateFileState, this.results);

        core0.setCoreReference(core1);
        core1.setCoreReference(core0);

        //Thread thread0 = new Thread(core0, Codes.THREAD_0);
        Thread thread1 = new Thread(core0, Codes.THREAD_1);
        Thread thread2 = new Thread(core1, Codes.THREAD_2);
        //thread0.start();
        thread1.start();
        thread2.start();

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        rw.setVisible(true);

        Vector<Integer> memoryContent = this.dataMemory.getMemory();

        System.out.println("Memoria antes");

        // Impresión de prueba de la memoria de datos
        for(int i = 0; i < Codes.DATA_MEM_CAPACITY; i++){
            System.out.print(memoryContent.get(i) + " ");
            if(((i + 1) % 4) == 0){
                System.out.println();
            }
        }

        System.out.println("Cache");

        for(int i = 0; i < core1.getDataCache().getCache().size(); i++){
            for(int j = 0; j < core1.getDataCache().getCache().get(i).size(); j++){
                System.out.println(core1.getDataCache().getCache().get(i).get(j));
            }
            System.out.println("TAG " + core1.getDataCache().getTag(i));
            System.out.println("STATE " + core1.getDataCache().getState(i));
            System.out.println();
        }

        System.out.println("Memoria despues");

        for(int i = 0; i < Codes.DATA_MEM_CAPACITY; i++){
            System.out.print(memoryContent.get(i) + " ");
            if(((i + 1) % 4) == 0){
                System.out.println();
            }
        }

        System.out.println("Registers");

        Vector<Registers> contexts = core0.getContextsList();

        for(int j = 0; j < contexts.size(); j++) {
            System.out.println("\nHilillo " + core0.getTakenFilesID().get(j));
            for (int i = 0; i < 32; i++) {
                System.out.println("R" + i + ": " + contexts.get(j).getRegister(i) + " ");
            }
        }

        contexts = core1.getContextsList();

        for(int j = 0; j < contexts.size(); j++) {
            System.out.println("\nHilillo " + core1.getTakenFilesID().get(j));
            for (int i = 0; i < 32; i++) {
                System.out.println("R" + i + ": " + contexts.get(j).getRegister(i) + " ");
            }
        }
    }

    public Vector<Integer> getRegistersByFileID(int fileID){
        return this.results.get(fileID).getRegisters();
    }
}
