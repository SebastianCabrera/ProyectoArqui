import Cores.DualCore;
import Cores.SingleCore;
import Enums.Codes;
import Structures.InstructionMemory;

import java.io.*;
import java.util.Vector;

public class Program {
    DualCore core0;
    SingleCore core1;
    InstructionMemory memory;

    public Program(){
        memory = new InstructionMemory();
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
                            //Guardar una por una en insMem (FALTA)

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
}
