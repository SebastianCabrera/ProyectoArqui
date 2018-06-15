package Structures;

import Abstracts.Memory;
import Enums.Codes;

import java.util.Vector;

public class InstructionMemory extends Memory {


    public InstructionMemory() {
        super(Codes.INSTRUCTION_MEM_CAPACITY);
    }
}
