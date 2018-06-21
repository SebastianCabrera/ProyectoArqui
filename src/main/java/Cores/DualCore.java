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
    public DualCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> threadStates){
        super(Codes.BLOCKS_IN_CACHE_0, insMem, dataMem, programBarrier, threadStates);
    }

    @Override
    public void run(){
        // Testing code
        /*this.clock++;
        System.out.println("In C0");
        System.out.println("Clock in C0:" + this.clock);
        System.out.println("Clock in C1 from C0: " + this.coreRefence.getClock() + "\n");*/

        // Averiguar cual thread soy
        if(this.getName().equals(Codes.THREAD_0)){ // comprueba que el hilo sea el 0
            do{
                Vector<Integer> instruction; // vector de enteros con instruccion



            }while(true);
        }

        super.run(); // llama a super para que haga run
    } // llave que cierra el metodo run
} // llave que cierra la clase

// fin de la clase
