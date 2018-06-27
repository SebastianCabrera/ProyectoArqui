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
            memory.add(Codes.EMPTY_MEMORY);
        }
    }

    public Vector<Integer> getMemory(){
        return this.memory;
    }

    protected int getBlockBegin(int direction){
        if(direction % 16 != 0){
            return direction - ((direction % 16) * (direction / 16));
        }else{
            return direction;
        }
    }
}
