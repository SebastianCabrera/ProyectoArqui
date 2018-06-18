package Structures;

import Enums.Codes;

import java.util.Vector;

public class Registers {
    private Vector<Integer> registers;

    public Registers() {
        this.registers = new Vector<>(Codes.TOTAL_REGISTERS);

        for(int i = 0; i < Codes.TOTAL_REGISTERS; i++){
            this.registers.add(i, Codes.EMPTY_REGISTER);
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
