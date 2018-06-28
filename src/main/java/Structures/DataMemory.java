package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataMemory extends Memory{

    private ReentrantLock memoryBus;

    public DataMemory() {
        super(Codes.DATA_MEM_CAPACITY);
        this.memoryBus = new ReentrantLock();
    }

    public void setBlock(int direction, Vector<Integer> block){
        int blockBeginDir = this.getBlockBegin(direction);

        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            this.memory.set(this.getRealDirection(i, blockBeginDir), block.get(i));
        }
    }

    public Vector<Integer> getBlock(int direction){
        Vector<Integer> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

        int blockBeginDir = this.getBlockBegin(direction);

        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            block.add(this.memory.get(this.getRealDirection(i, blockBeginDir)));
        }

        return block;
    }

    public ReentrantLock getMemoryBusLock(){
        return this.memoryBus;
    }

    private int getRealDirection(int index, int direction){
        return (direction / Codes.DATA_WORD_BYTES) + index;
    }
}
