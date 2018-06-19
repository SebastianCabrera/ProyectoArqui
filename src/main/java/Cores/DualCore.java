package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class DualCore extends Core {
    public DualCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier){
        super(Codes.BLOCKS_IN_CACHE_0, insMem, dataMem, programBarrier);
    }

    @Override
    public void run(){
        // Testing code
        this.clock++;
        System.out.println("In C0");
        System.out.println("Clock in C0:" + this.clock);
        System.out.println("Clock in C1 from C0: " + this.coreRefence.getClock() + "\n");

        super.run();
    }
}
