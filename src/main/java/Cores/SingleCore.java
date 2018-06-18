package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.InstructionMemory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class SingleCore extends Core {
    public SingleCore(InstructionMemory insMem, CyclicBarrier programBarrier){
        super(Codes.BLOCKS_IN_CACHE_1, programBarrier);
        this.instructionMemory = insMem;
    }

    @Override
    public void run(){
        this.clock++;
        System.out.println("Clock in C1:" + this.clock);

        super.run();
    }
}
