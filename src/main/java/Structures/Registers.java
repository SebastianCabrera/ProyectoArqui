package Structures;

import Enums.Codes;

import java.util.Vector;

/**
 * Clase que representa a los registros de un núcleo.
 */
public class Registers {
    private Vector<Integer> registers;  // Vector que almacena los registros.

    /**
     * Constructor de los registros. Se inicializan vacíos.
     * NOTA: El PC siempre se almacena al final de los 32 registros, esto porque
     * necesitaba ser manejado como una referenca, y los primitivos solo pueden
     * ser pasados por copia.
     */
    public Registers() {
        this.registers = new Vector<>(Codes.TOTAL_REGISTERS);

        for(int i = 0; i < Codes.TOTAL_REGISTERS; i++){
            this.registers.add(Codes.EMPTY_REGISTER);
        }
    }

    /**
     * Constructor por copias. Necesario para guardar el contexto porque sino
     * solo se guarda una referencia, y al modificar el original, se modifican
     * los contextos también
     * @param regValues Los registros a copiar
     */
    public Registers(Registers regValues){
        this.registers = new Vector<>(Codes.TOTAL_REGISTERS);

        for(int i = 0; i < Codes.TOTAL_REGISTERS; i++){
            this.registers.add(regValues.getRegister(i));
        }
    }

    // Métodos modificadores

    /**
     * Modifica el contenido de un registro
     * @param register El número de regsitro
     * @param value El valor a almacenar en el registro
     */
    public void setRegister(int register, int value){
        this.registers.set(register, value);
    }

    // Métodos obtenedores

    /**
     * Obtiene el contenido de un registro
     * @param register El número de regsitro
     * @return El valor en el registro
     */
    public int getRegister(int register){
        return this.registers.get(register);
    }

    // Métodos dedicados a la impresión de datos

    /**
     * Obtiene los registros como vector de enteros.
     * @return Los registros actuales.
     */
    public Vector<Integer> getRegisters(){
        return this.registers;
    }
}
