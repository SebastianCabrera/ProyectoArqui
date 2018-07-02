package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa a la memoria caché de datos.
 */
public class DataCache extends Cache {

    private Vector<Vector<Integer>> cache;  // La estructura de la caché. El vector externo son las columnas y el interno las palabras
    private Vector<Character> states;     // Estado de cada bloque (I/C/M).
    private Vector<ReentrantLock> locks;    // Candados para cada posición
    private ReentrantLock cacheBus;         // Candado que representa el bus a la caché

    /**
     * Constructor de la caché
     * @param totalBlocks Tamaño de la caché en columnas.
     */
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
            this.tags.add(Codes.EMPTY_BLOCK);
            this.states.add(Codes.I);
            this.locks.add(new ReentrantLock());

            this.cache.add(block);
        }
    }

    // Métodos modificadores.

    /**
     * Carga un bloque en la caché
     * @param position Posición en la cual se encuentra el bloque.
     * @param block Bloque a almacenar
     */
    public void setBlock(int position, Vector<Integer> block){
        this.cache.set(position, block);
    }

    /**
     * Almacena una palabra en caché
     * @param position Posición en la cual se encuentra el bloque
     * @param numWord Posición del bloque en el cual se encuentra la palabra
     * @param word La palabra a almacenar
     */
    public void setWord(int position, int numWord, int word){
        this.cache.get(position).set(numWord, word);
    }

    /**
     * Modifica el estado de un bloque en la caché
     * @param position La posición donde se encuentra el bloque en caché
     * @param state El nuevo estado para el bloque posición
     */
    public void setState(int position, char state){
        this.states.set(position, state);
    }

    // Métodos obtenedores

    /**
     * Obtiene un bloque de la caché
     * @param position Posición en la cual se encuentra el bloque
     * @return Bloque de la caché
     */
    public Vector<Integer> getBlock(int position){
        return this.cache.get(position);
    }

    /**
     * Obtiene una palabra de la caché
     * @param position Posición en la cual se encuentra el bloque
     * @param numWord Posición del bloque en el cual se encuentra la palabra
     * @return La palabra del bloque
     */
    public int getWord(int position, int numWord){
        return this.cache.get(position).get(numWord);
    }

    /**
     * Obtiene el estado de un bloque en la caché
     * @param position La posición donde se encuentra el bloque en caché
     * @return El estado actual del bloque en la caché
     */
    public char getState(int position){
        return this.states.get(position);
    }

    /**
     * Obtiene el candado asociado a una posición de la caché
     * @param position La posición de la caché
     * @return El candado asociado a la posición
     */
    public ReentrantLock getPositionLock(int position){
        return this.locks.get(position);
    }

    /**
     * Obtiene el candado que simula el bus hacia la caché
     * @return El candado de la caché
     */
    public ReentrantLock getCacheBusLock(){
        return this.cacheBus;
    }

    // Métodos dedicados a la impresión de datos

    /**
     * Obtiene la estructura que compone a la caché para ser impresa
     * @return La estructura de la caché
     */
    public Vector<Vector<Integer>> getCache(){
        return this.cache;
    }

    /**
     * Obtiene todos los estados de la caché por posición en un vector
     * @return El vector con todos los estados
     */
    public Vector<Character> getStates(){
        return this.states;
    }

    /**
     * Obtiene todas las etiquetas de la caché por posición en un vector
     * @return El vector con todas las etiquetas
     */
    public Vector<Integer> getTags(){
        return this.tags;
    }
}
