package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.InstructionMemory;
import Structures.Registers;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class DualCore extends Core {
    Vector<Registers> contextsList;

    public DualCore(InstructionMemory insMem, CyclicBarrier programBarrier){
        super(Codes.BLOCKS_IN_CACHE_0, programBarrier);
        contextsList = new Vector<>();
        this.instructionMemory = insMem;
    }

    @Override
    public void run(){
        this.clock++;
        System.out.println("Clock in C0:" + this.clock);

        super.run();
    }
}
