package Structures;

import Enums.Codes;

import java.util.Vector;

public class Registers {
    private Vector<Integer> registers;

    //Todo: Considerar borrar esto
    public Registers() {
        this.registers = new Vector<>(Codes.TOTAL_REGISTERS);

        for(int i = 0; i < Codes.TOTAL_REGISTERS; i++){
            this.registers.add(Codes.EMPTY_REGISTER);
        }
    }

    // Constructor por copias. Necesario para guardar el contexto porque sino
    // solo se guarda una referencia, y al modificar el original, se modifican
    // los contextos también
    public Registers(Registers regValues){
        this.registers = new Vector<>(Codes.TOTAL_REGISTERS);

        for(int i = 0; i < Codes.TOTAL_REGISTERS; i++){
            this.registers.add(regValues.getRegister(i));
        }
    }

    public void setRegister(int register, int value){
        this.registers.set(register, value);
    }

    public int getRegister(int register){
        return this.registers.get(register);
    }

    public Vector<Integer> getRegisters(){
        return this.registers;
    }
}
