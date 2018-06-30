package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class DualCore extends Core {
    public DualCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> fbd,
                    Vector<Integer> ft, Semaphore s, Vector<Registers> res, int quantum, Queue<Registers> contextsList,
                    Queue<Integer> contextsListID, CyclicBarrier cycleBarrier){
        super(Codes.BLOCKS_IN_CACHE_0, insMem, dataMem, programBarrier, fbd, ft, s, res, contextsList, contextsListID, cycleBarrier, quantum);
        this.coreId = Codes.CORE_0;
    }

    @Override
    public void run(){

        // Averiguar cual thread soy
        if(this.getName().equals(Codes.THREAD_0)){
            do{
                Vector<Integer> instruction;



            }while(true);
        }

        super.run();
    }
}

