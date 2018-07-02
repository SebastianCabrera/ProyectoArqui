package Abstracts;

import java.util.Vector;

/**
 * Clase abstracta que representa a la memoria caché. Posee atributos y métodos que ambos tipos de
 * cachés utilizan.
 */
public abstract class Cache {

    // Variables.

    protected Vector<Integer> tags; // Etiquetas de cada posición. Representan el número de bloque.

    // Métodos modificadores.

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
     * Obtiene la etiqueta de una posición en la caché
     * @param position La posición de la cual se desea conocer su etiqueta
     * @return La etiqueta que representa el número de bloque en la posición
     */
    public int getTag(int position){
        return this.tags.get(position);
    }
}
