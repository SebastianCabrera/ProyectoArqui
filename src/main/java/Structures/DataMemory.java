package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataMemory extends Memory{

    private Lock memoryBus;

    public DataMemory() {
        super(Codes.DATA_MEM_CAPACITY);
        this.memoryBus = new ReentrantLock();
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

    public Lock getMemoryBusLock(){
        return this.memoryBus;
    }
}
