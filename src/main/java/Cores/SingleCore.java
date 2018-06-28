package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class SingleCore extends Core {
    public SingleCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> fbd, Vector<Boolean> ft, Semaphore s, Vector<Registers> res){
        super(Codes.BLOCKS_IN_CACHE_1, insMem, dataMem, programBarrier, fbd, ft, s, res);
        this.coreId = Codes.CORE_1;
    }

    @Override
    public void run(){
        int block = 0;
        int word = 0;
        int direction = 0;
        int position = 0;

        for(int i = 0; i < this.filesBeginDirection.size() - 1; i++) {

            try {
                this.semaphore.acquire();

                if(!this.takenFiles.get(i)){
                    this.takenFiles.set(i, true);
                    this.takenFilesID.add(i);
                    this.registers.setRegister(Codes.PC, this.filesBeginDirection.get(i) - 384);

                    this.semaphore.release();

                    boolean cycle = true;

                    while (cycle) {

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

                    this.results.set(i, this.getContextsList().lastElement());

                }else{
                    this.semaphore.release();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private int calculateInstructionBlock(int direction){
        return direction / 16;
    }

    private int calculateWord(int direction){
        return (direction % 16) / 4;
    }
}
