package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;

/**
 * Clase que representa a la memoria caché de instrucciones.
 */
public class InstructionCache extends Cache{

    private Vector<Vector<Vector<Integer>>> cache;  // La estructura de la caché. El vector externo son las columnas,
                                                    // el siguiente las palabras, y el interno los 4 enteros de cada palabra

    /**
     * Constructor de la caché
     * @param totalBlocks Tamaño de la caché en columnas.
     */
    public InstructionCache(int totalBlocks) {

        this.cache = new Vector<>(totalBlocks);
        this.tags = new Vector<>(totalBlocks);

        // Inicializa la caché
        for(int i = 0; i < totalBlocks; i++){

            // Crea un vector de 4 palabras
            Vector<Vector<Integer>> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

            // Inserta palabras vacías de 4 posiciones para datos
            for(int j = 0; j < 4; j++){
                Vector<Integer> word = new Vector<>(Codes.INSTRUCTIONS_WORD_SIZE);

                for(int k = 0; k < Codes.INSTRUCTIONS_WORD_SIZE; k++){
                    word.add(Codes.EMPTY_CACHE);
                }

                block.add(j, word);
            }

            // Inicializa etiquetas y estados
            this.tags.add(Codes.EMPTY_BLOCK);

            this.cache.add(block);
        }
    }

    // Métodos modificadores

    /**
     * Carga un bloque en la caché
     * @param position Posición en la cual se encuentra el bloque.
     * @param block Bloque a almacenar
     */
    public void setBlock(int position, Vector<Vector<Integer>> block){
        this.cache.set(position, block);
    }

    /**
     * Almacena una palabra en caché
     * @param position Posición en la cual se encuentra el bloque
     * @param numWord Posición del bloque en el cual se encuentra la palabra
     * @param word La palabra a almacenar
     */
    public void setWord(int position, int numWord, Vector<Integer> word){
        this.cache.get(position).set(numWord, word);
    }

    // Métodos obtenedores

    /**
     * Obtiene un bloque de la caché
     * @param position Posición en la cual se encuentra el bloque
     * @return Bloque de la caché
     */
    public Vector<Vector<Integer>> getBlock(int position){
        return this.cache.get(position);
    }

    /**
     * Obtiene una palabra de la caché
     * @param position Posición en la cual se encuentra el bloque
     * @param numWord Posición del bloque en el cual se encuentra la palabra
     * @return La palabra del bloque
     */
    public Vector<Integer> getWord(int position, int numWord){
        return this.cache.get(position).get(numWord);
    }
}
