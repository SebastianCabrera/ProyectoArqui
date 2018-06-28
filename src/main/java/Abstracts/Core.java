package Abstracts;

import Instructions.Instructions;
import Structures.*;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Core extends Thread{
    protected Vector<Registers> contextsList;

    protected DataMemory dataMemory;
    protected InstructionMemory instructionMemory;

    protected DataCache dataCache;
    protected InstructionCache instructionCache;

    protected Registers registers;

    protected int clock;

    protected CyclicBarrier barrier;

    protected Core coreReference;

    protected int coreId;

    protected Instructions instructions;

    protected Vector<Integer> filesBeginDirection;
    protected Vector<Boolean> takenFiles;

    protected Semaphore semaphore;

    protected Vector<Integer> takenFilesID;

    protected Vector<Registers> results;

    protected Core(int cacheSize, InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> fbd, Vector<Boolean> tf, Semaphore s, Vector<Registers> res){
        this.contextsList = new Vector<>();

        this.barrier = programBarrier;

        this.instructionMemory = insMem;
        this.dataMemory = dataMem;

        this.dataCache = new DataCache(cacheSize);
        this.instructionCache = new InstructionCache(cacheSize);

        this.registers = new Registers();

        this.clock = 0;

        this.instructions = new Instructions();

        this.filesBeginDirection = fbd;
        this.takenFiles = tf;

        this.semaphore = s;

        this.takenFilesID = new Vector<>();

        this.results = res;
    }

    public void setCoreReference(Core reference){
        this.coreReference = reference;
    }

    @Override
    public void run(){
        try {
            this.barrier.await();
        }catch (InterruptedException |BrokenBarrierException e){
            System.err.println(e.toString());
        }
    }

    public int getClock(){
        return this.clock;
    }

    public DataCache getDataCache(){
        return this.dataCache;
    }

    public InstructionCache getInstructionCache(){
        return this.instructionCache;
    }

    public int getCoreId(){
        return this.coreId;
    }

    public Core getOtherCoreReference(){
        return this.coreReference;
    }

    public Registers getRegisters(){
        return this.registers;
    }

    public void addContext(Registers registers){
        this.contextsList.add(registers);
    }

    public Vector<Registers> getContextsList(){
        return this.contextsList;
    }

    public Vector<Integer> getTakenFilesID(){
        return this.takenFilesID;
    }
}