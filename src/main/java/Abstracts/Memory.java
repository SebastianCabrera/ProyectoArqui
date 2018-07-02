package Abstracts;

import Enums.Codes;

import java.util.Vector;

/**
 * Clase abstracta que representa a la memoria principal. Posee atributos y métodos que ambos tipos de
 * memoria utilizan.
 */
public abstract class Memory {

    // Variables

    protected Vector<Integer> memory;   // La memoria almacenada como un vector continuo de enteros.

    /**
     * Constructor de la estructura. Crea la memoria con un tamaño específico dependiendo se si es la memoria de datos
     * o de instrucciones y la llena con 1 en cada posición.
     * @param size El tamaño de la memoria.
     */
    public Memory(int size) {
        this.memory = new Vector<>(size);

        for(int i = 0; i < size; i++){
            memory.add(Codes.EMPTY_MEMORY);
        }
    }
}
