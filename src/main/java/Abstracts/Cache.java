package Abstracts;

import Enums.Codes;

import java.util.Vector;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Cache {

    private Vector<Vector<Word>> cache;
    private Vector<Integer> tags;
    private Vector<Integer> states;

    public Cache(int totalBlocks, int wordSize){

        this.cache = new Vector<>(totalBlocks);

        // Inicializa la caché
        for(int i = 0; i < totalBlocks; i++){

            // Crea un vector de 4 palabras
            Vector<Word> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

            // Inserta palabras vacías de 1 posición para datos y de 4 para instrucciones
            for(int j = 0; j < 4; j++){
                block.set(j, new Word(wordSize));
            }

            // Inicializa etiquetas y estados
            tags.set(i, Codes.EMPTY);
            states.set(i, Codes.I);

            this.cache.set(i, block);
        }
    }

    /**
     * Clase que representa una palabra. El tamaño del vector es 1 o 4 dependiendo del tipo de caché
     */
    private class Word{
        private Vector<Integer> wordContent;

        private Word(int size){
            this.wordContent = new Vector<>(size);

            for(int i = 0; i < size; i++){
                this.wordContent.set(i, -1);
            }
        }

        private Vector<Integer> getContent(){
            return this.wordContent;
        }
    }
}
