package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;

public class InstructionCache extends Cache{

    private Vector<Vector<Vector<Integer>>> cache;

    public InstructionCache(int totalBlocks) {

        this.cache = new Vector<>(totalBlocks);

        // Inicializa la caché
        for(int i = 0; i < totalBlocks; i++){

            // Crea un vector de 4 palabras
            Vector<Vector<Integer>> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

            // Inserta palabras vacías de 4 posiciones para datos
            for(int j = 0; j < 4; j++){
                Vector<Integer> word = new Vector<>(Codes.INSTRUCTIONS_WORD_SIZE);

                for(int k = 0; k < Codes.INSTRUCTIONS_WORD_SIZE; k++){
                    word.add(Codes.EMPTY);
                }

                block.set(j, word);
            }

            // Inicializa etiquetas y estados
            this.tags.add(Codes.EMPTY);
            this.states.add(Codes.I);

            this.cache.add(block);
        }
    }

    public void setBlock(int numBlock, Vector<Vector<Integer>> block){
        this.cache.set(numBlock, block);
    }

    public Vector<Vector<Integer>> getBlock(int numBlock){
        return this.cache.get(numBlock);
    }

    public void setWord(int numBlock, int numWord, Vector<Integer> word){
        this.cache.get(numBlock).set(numWord, word);
    }

    public Vector<Integer> getWord(int numBlock, int numWord){
        return this.cache.get(numBlock).get(numWord);
    }

    public Vector<Vector<Vector<Integer>>> getCache(){
        return this.cache;
    }
}
