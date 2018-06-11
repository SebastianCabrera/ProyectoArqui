package Structures;

import java.util.Vector;

public class Registers {
    private Vector<Integer> registers;

    public Registers() {
        this.registers = new Vector<>(33);
    }

    public void setRegister(int register, int value){
        this.registers.set(register, value);
    }

    public int getRegister(int register){
        return this.registers.get(register);
    }
}
