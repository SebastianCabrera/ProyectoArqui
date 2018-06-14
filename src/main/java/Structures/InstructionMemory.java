package Structures;

import Interfaces.MemoryInterface;

import java.util.Vector;

public class InstructionMemory implements MemoryInterface {

    Vector insMemory;

    @Override
    public void createMemory(int size) {
        insMemory = new Vector<String>(size);
    }
}
