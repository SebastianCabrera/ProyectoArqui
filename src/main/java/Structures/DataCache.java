package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;

public class DataCache extends Cache {

    private Vector<Vector<Integer>> cache;

    public DataCache(int totalBlocks, int wordSize){
        this.cache = new Vector<>(totalBlocks);

        // Inicializa la caché
        for(int i = 0; i < totalBlocks; i++){

            // Crea un vector de 4 palabras
            Vector<Integer> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

            // Inserta palabras vacías de 1 posición para datos
            for(int j = 0; j < 4; j++){
                block.set(j, Codes.EMPTY);
            }

            // Inicializa etiquetas y estados
            this.tags.set(i, Codes.EMPTY);
            this.states.set(i, Codes.I);

            this.cache.set(i, block);
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
}
