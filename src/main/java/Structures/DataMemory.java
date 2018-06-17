package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;

public class DataMemory extends Memory{

    public DataMemory() {
        super(Codes.DATA_MEM_CAPACITY);
    }

    public void setBlock(int direction, Vector<Integer> block){
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            this.memory.set(this.getRealDirection(i, direction), block.get(i));
        }
    }

    public Vector<Integer> getBlock(int direction){
        Vector<Integer> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            block.set(i, this.memory.get(this.getRealDirection(i, direction)));
        }

        return block;
    }
}
