package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class SingleCore extends Core {
    public SingleCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier){
        super(Codes.BLOCKS_IN_CACHE_1, insMem, dataMem, programBarrier);
        this.coreId = Codes.CORE_1;
    }

    @Override
    public void run(){
        // Testing code
        //this.clock++;
        //System.out.println("In C1");
        //System.out.println("Clock in C1:" + this.clock);
        //System.out.println("Clock in C0 from C1: " + this.coreRefence.getClock() + "\n");


        // Prueba leyendo desde memoria. Debería ser desde caché
        int block = 0;
        int word = 0;
        int direction = 0;
        int position = 0;

        for(int i = 0; i < 6; i++) {

            boolean cycle = true;

            while (cycle) {
                //System.out.print("INST ");
                // Crear método en memoria para cargar palabra, no así.
                direction = this.registers.getRegister(Codes.PC) + 384;
                block = this.calculateInstructionBlock(this.registers.getRegister(Codes.PC));
                word = this.calculateWord(direction);
                position = block % 4;

                if (this.instructionCache.getTag(position) != block) {
                    this.instructionCache.setBlock(position, this.instructionMemory.getBlock(direction));
                    this.instructionCache.setTag(position, block);
                }

                Vector<Integer> instruction = this.instructionCache.getWord(position, word);

                // Freno por ahora
                if (instruction.toString().equals("[63, 0, 0, 0]")) {
                    cycle = false;
                }

                System.out.println(instruction);
                this.instructions.decode(this.registers, instruction, this.dataMemory, this);

                System.out.println(block);
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //super.run();
    }

    private int calculateInstructionBlock(int direction){
        return direction / 16;
    }

    private int calculateWord(int direction){
        return (direction % 16) / 4;
    }
}
