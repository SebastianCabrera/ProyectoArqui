import Abstracts.Core;
import Cores.DualCore;
import Cores.SingleCore;
import Enums.Codes;
import Structures.InstructionMemory;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

public class Program {
    private DualCore core0;
    private SingleCore core1;
    private InstructionMemory memory;
    private int clock;
    private final CyclicBarrier barrier;

    public Program(){
        this.memory = new InstructionMemory();
        this.clock = 0;
        this.barrier = new CyclicBarrier(Codes.TOTAL_CORES);
    }

    public void loadInstructions(String[] files){
        int memoryPosition = 0;

        for(int i = 0; i < files.length; i++){
            File encyptedFile = new File(".\\Hilillos\\" + files[i]);

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

                            this.memory.setWordData(decodedLine, memoryPosition);

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
        }

        Vector<Integer> memoryContent = this.memory.getMemory();

        // Impresión de prueba de la memoria de instrucciones
        for(int i = 0; i < Codes.INSTRUCTION_MEM_CAPACITY; i++){
            System.out.print(memoryContent.get(i) + " ");
            if(((i + 1) % 4) == 0){
                System.out.println();
            }
        }
    }

    public void runProgram(){


        core0 = new DualCore(this.memory, this.barrier);
        core1 = new SingleCore(this.memory, this.barrier);


        Thread thread0 = new Thread(core0);
        Thread thread1 = new Thread(core0);
        Thread thread2 = new Thread(core1);
        thread0.start();
        thread1.start();
        thread2.start();
    }
}
