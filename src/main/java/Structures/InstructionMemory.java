package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;

public class InstructionMemory extends Memory {


    public InstructionMemory() {
        super(Codes.INSTRUCTION_MEM_CAPACITY);
    }

    public void setBlock(int virtualDirection, Vector<Vector<Integer>> block){
        int direction = this.getDirection(virtualDirection);

        int index = 0;
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            for(int j = 0; j < Codes.INSTRUCTIONS_WORD_SIZE; j++){
                this.memory.set(this.getRealDirection(index, direction), block.get(i).get(j));
                index++;
            }
        }
    }

    public Vector<Vector<Integer>> getBlock(int virtualDirection){
        int direction = this.getDirection(virtualDirection);

        Vector<Vector<Integer>> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

        int index = 0;
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            for(int j = 0; i < Codes.CACHE_BLOCK_SIZE; j++){
                block.get(i).set(j, this.memory.get(this.getRealDirection(index, direction)));
                index++;
            }
        }

        return block;
    }

    private int getDirection(int virtualDirection){
        return virtualDirection * Codes.INSTRUCTION_MEM_BEGIN;
    }

    public void setWordData(Vector<Integer> word, int position){
        for(int i = 0; i < Codes.INSTRUCTIONS_WORD_SIZE; i++){
            this.memory.set((i + position), word.get(i));
        }
    }
}
