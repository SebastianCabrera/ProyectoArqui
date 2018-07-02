package Abstracts;

import java.util.Vector;

/**
 * Clase abstracta que representa a la memoria caché. Posee atributos y métodos que ambos tipos de
 * cachés utilizan.
 */
public abstract class Cache {

    // Variables.

    protected Vector<Integer> tags;         // Etiquetas de cada posición. Representan el número de bloque.
    protected Vector<Character> states;     // Estado de cada bloque (I/C/M).

    // Métodos modificadores.

    /**
     * Modifica el estado de un bloque en la caché
     * @param position La posición donde se encuentra el bloque en caché
     * @param state El nuevo estado para el bloque posición
     */
    public void setState(int position, char state){
        this.states.set(position, state);
    }

    /**
     * Modifica la etiqueta de una posición en la caché
     * @param position La posición donde se colocará la nueva etiqueta
     * @param tag La etiqueta que representa el número de bloque a colocar en la posición
     */
    public void setTag(int position, int tag){
        this.tags.set(position, tag);
    }

    // Métodos obtenedores.

    /**
     * Obtiene el estado de un bloque en la caché
     * @param position La posición donde se encuentra el bloque en caché
     * @return El estado actual del bloque en la caché
     */
    public char getState(int position){
        return this.states.get(position);
    }

    /**
     * Obtiene la etiqueta de una posición en la caché
     * @param position La posición de la cual se desea conocer su etiqueta
     * @return La etiqueta que representa el número de bloque en la posición
     */
    public int getTag(int position){
        return this.tags.get(position);
    }

    // Métodos dedicados a la impresión de datos

    /**
     * Obtiene todas las etiquetas de la caché por posición en un vector
     * @return El vector con todas las etiquetas
     */
    public Vector<Integer> getTags(){
        return this.tags;
    }

    /**
     * Obtiene todos los estados de la caché por posición en un vector
     * @return El vector con todos los estados
     */
    public Vector<Character> getStates(){
        return this.states;
    }
}
