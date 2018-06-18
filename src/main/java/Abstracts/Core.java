package Abstracts;

import Enums.Codes;
import Structures.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Core implements Runnable{
    protected DataMemory dataMemory;
    protected InstructionMemory instructionMemory;

    protected DataCache dataCache;
    protected InstructionCache instructionCache;

    protected Registers registers;

    protected int clock;

    protected CyclicBarrier barrier;

    protected Core(int cacheSize, CyclicBarrier programBarrier){
        this.barrier = programBarrier;

        this.dataMemory = new DataMemory();

        this.dataCache = new DataCache(cacheSize);
        this.instructionCache = new InstructionCache(cacheSize);

        this.registers = new Registers();

        this.clock = 0;
    }

    @Override
    public void run(){
        try {
            this.barrier.await();
        }catch (InterruptedException |BrokenBarrierException e){
            System.err.println(e.toString());
        }
    }
}
