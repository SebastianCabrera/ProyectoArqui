package Abstracts;

import Enums.Codes;

import java.util.Vector;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Memory {
    protected Vector<Integer> memory;

    public Memory(int size) {
        this.memory = new Vector<>(size);

        for(int i = 0; i < size; i++){
            memory.set(i, Codes.EMPTY);
        }
    }

    protected int getRealDirection(int index, int direction){
        return (index * Codes.INSTRUCTIONS_WORD_BYTES) + direction;
    }

    public Vector<Integer> getMemory(){
        return this.memory;
    }
}
