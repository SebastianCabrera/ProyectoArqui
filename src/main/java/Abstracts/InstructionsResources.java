package Abstracts;

import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by J.A Rodríguez on 25/06/2018.
 */
public abstract class InstructionsResources {
    protected Vector<ReentrantLock> reservedStructures;

    protected InstructionsResources(){
        reservedStructures = new Vector<>();
    }

    protected int calculateBlock(int memDirection){
        return memDirection / Codes.DATA_BLOCK_BYTES;
    }

    protected int calculateWord(int memDirection){
        return (memDirection % Codes.DATA_BLOCK_BYTES) / Codes.DATA_WORD_BYTES;
    }

    protected int calculateCachePosition(int blockNumber, int coreId){
        switch (coreId){
            case Codes.CORE_0:
                return (blockNumber % Codes.BLOCKS_IN_CACHE_0);
            default:
                return (blockNumber % Codes.BLOCKS_IN_CACHE_1);
        }
    }

    protected int restart(){
        for(int i = 0; i < this.reservedStructures.size(); i++){
            if(this.reservedStructures.get(i).isLocked()) {
                this.reservedStructures.get(i).unlock();
            }
        }
        this.reservedStructures.clear();

        return Codes.FAILURE;
    }
}
