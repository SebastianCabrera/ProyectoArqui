package Structures;

import Enums.Codes;

import java.util.Vector;

public class DataCache {
    private Vector<Vector<Integer>> cache;

    public void DataCache(int blocks){
        this.cache = new Vector<>(blocks);

        for(int i = 0; i < blocks; i++){

            Vector<Integer> block = new Vector<>(6);
            for(int j = 0; j < 4; j++){
                block.set(j, Codes.EMPTY);
            }
            block.set(4, Codes.I);
            block.set(5, Codes.EMPTY);

            this.cache.set(i, block);
        }
    }
}
