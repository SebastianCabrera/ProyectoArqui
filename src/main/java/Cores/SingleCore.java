package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class SingleCore extends Core {
    public SingleCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier){
        super(Codes.BLOCKS_IN_CACHE_1, insMem, dataMem, programBarrier);
    }

    @Override
    public void run(){
        // Testing code
        this.clock++;
        System.out.println("In C1");
        System.out.println("Clock in C1:" + this.clock);
        System.out.println("Clock in C0 from C1: " + this.coreRefence.getClock() + "\n");

        super.run();
    }
}
