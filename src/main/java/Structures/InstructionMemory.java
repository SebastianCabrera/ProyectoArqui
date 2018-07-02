package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa a la memoria de instrucciones.
 */
public class InstructionMemory extends Memory {

    private ReentrantLock memoryBus;    // Candado que representa el bus a la memoria

    /**
     * Constructor de la clase. Se crea con la capacidad definida.
     */
    public InstructionMemory() {
        super(Codes.INSTRUCTION_MEM_CAPACITY);
        this.memoryBus = new ReentrantLock();
    }

    // Métodos modificadores

    /**
     * Permite subir un bloque a la memoria
     * @param direction La dirección inicial del bloque
     * @param block El bloque a subir
     */
    public void setBlock(int direction, Vector<Vector<Integer>> block){
        int index = 0;
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            for(int j = 0; j < Codes.INSTRUCTIONS_WORD_SIZE; j++){
                this.memory.set((direction + index), block.get(i).get(j));
                index++;
            }
        }
    }

    /**
     * Permite cargar los datos a una palabra
     * @param word La palabra a cargar
     * @param position La posición de la palabra en memoria
     */
    public void setWordData(Vector<Integer> word, int position){
        for(int i = 0; i < Codes.INSTRUCTIONS_WORD_SIZE; i++){
            this.memory.set((i + position), word.get(i));
        }
    }

    // Métodos obtenedores

    /**
     * Obtiene un bloque de la memoria
     * @param direction La dirección inicial del bloque
     * @return El bloque de memoria
     */
    public Vector<Vector<Integer>> getBlock(int direction){
        int index = 0;
        Vector<Vector<Integer>> block = new Vector<>(Codes.CACHE_BLOCK_SIZE);
        for(int i = 0; i < Codes.CACHE_BLOCK_SIZE; i++){
            block.add(new Vector<>());
            for(int j = 0; j < Codes.CACHE_BLOCK_SIZE; j++){
                block.get(i).add(this.memory.get(direction + index));
                index++;
            }
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
}
