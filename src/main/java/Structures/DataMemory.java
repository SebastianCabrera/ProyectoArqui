package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa a la memoria de datos.
 */
public class DataMemory extends Memory{

    private ReentrantLock memoryBus;    // Candado que representa el bus a la memoria

    /**
     * Constructor de la clase. Se crea con la capacidad definida.
     */
    public DataMemory() {
        super(Codes.DATA_MEM_CAPACITY);
        this.memoryBus = new ReentrantLock();
    }

    // Métodos modificadores

    /**
     * Permite subir un bloque a la memoria
     * @param direction La dirección inicial del bloque
     * @param block El bloque a subir
     */
    public void setBlock(int direction, Vector<Integer> block){
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            this.memory.set((direction + i), block.get(i));
        }
    }

    // Métodos obtenedores

    /**
     * Obtiene un bloque de la memoria
     * @param direction La dirección inicial del bloque
     * @return El bloque de memoria
     */
    public Vector<Integer> getBlock(int direction){
        Vector<Integer> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);

        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            block.add(this.memory.get(direction + i));
        }

        return block;
    }

    /**
     * Obtiene el candado que simula el bus hacia la memoria
     * @return El candado de la memoria
     */
    public ReentrantLock getMemoryBusLock(){
        return this.memoryBus;
    }

    // Métodos dedicados a la impresión de datos

    /**
     * Obtiene la memoria como vector de enteros.
     * @return La memoria actual.
     */
    public Vector<Integer> getMemory(){
        return this.memory;
    }
}
