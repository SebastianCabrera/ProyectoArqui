package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataCache extends Cache {

    private Vector<Vector<Integer>> cache;
    private Vector<Lock> locks;
    private Lock cacheBus;

    public DataCache(int totalBlocks){
        this.cache = new Vector<>(totalBlocks);
        this.tags = new Vector<>(totalBlocks);
        this.states = new Vector<>(totalBlocks);
        this.locks = new Vector<>(totalBlocks);
        this.cacheBus = new ReentrantLock();

        // Inicializa la caché
        for(int i = 0; i < totalBlocks; i++){

            // Crea un vector de 4 palabras
            Vector<Integer> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

            // Inserta palabras vacías de 1 posición para datos
            for(int j = 0; j < 4; j++){
                block.add(Codes.EMPTY_CACHE);
            }

            // Inicializa etiquetas, estados y reservas vacías
            this.tags.add(Codes.EMPTY_CACHE);
            this.states.add(Codes.I);
            this.locks.add(new ReentrantLock());

            this.cache.add(block);
        }
    }

    public void setBlock(int numBlock, Vector<Integer> block){
        this.cache.set(numBlock, block);
    }

    public Vector<Integer> getBlock(int numBlock){
        return this.cache.get(numBlock);
    }

    public void setWord(int numBlock, int numWord, int word){
        this.cache.get(numBlock).set(numWord, word);
    }

    public int getWord(int numBlock, int numWord){
        return this.cache.get(numBlock).get(numWord);
    }

    public Vector<Vector<Integer>> getCache(){
        return this.cache;
    }

    public Lock getPositionLock(int position){
        return this.locks.get(position);
    }

    public Lock getCacheBusLock(){
        return this.cacheBus;
    }
}
