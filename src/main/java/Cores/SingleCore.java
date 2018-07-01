package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.InstructionMemory;
import Structures.Registers;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class SingleCore extends Core {
    public SingleCore(InstructionMemory insMem, DataMemory dataMem, CyclicBarrier programBarrier, Vector<Integer> fbd,
                      Vector<Integer> ft, Semaphore s, Vector<Registers> res, int quantum, Queue<Registers> contextsList,
                      Queue<Integer> contextsListID, CyclicBarrier cycleBarrier, int id){
        super(Codes.BLOCKS_IN_CACHE_1, insMem, dataMem, programBarrier, fbd, ft, s, res, contextsList, contextsListID, cycleBarrier, quantum);
        this.coreId = id;
    }

    @Override
    public void run(){
        int block = 0;
        int word = 0;
        int direction = 0;
        int position = 0;


            while(true) {

                try {

                    this.semaphore.acquire();

                    if(!contextsList.isEmpty()) {

                        this.registers = new Registers(contextsList.poll());
                        int fileID = contextsListID.poll();


                        if (this.takenFiles.get(fileID) != Codes.TAKEN) {
                            this.myTakenFiles.add(fileID);

                            if (this.takenFiles.get(fileID) == Codes.NOT_TAKEN) {
                                this.registers.setRegister(Codes.PC, this.filesBeginDirection.get(fileID) - Codes.INSTRUCTION_MEM_BEGIN);
                            }
                            this.takenFiles.set(fileID, Codes.TAKEN);

                            this.semaphore.release();

                            boolean cycle = true;

                            while (cycle) {

                                if (quantum == 0) {
                                    //Copiar contexto

                                    try{
                                        semaphore.acquire();

                                        quantum = 10;
                                        this.contextsList.add(new Registers(this.registers));
                                        this.contextsListID.add(fileID);
                                        this.takenFiles.set(fileID, Codes.IN_PROGRESS);

                                        semaphore.release();
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                    cycle = false;
                                } else {
                                    direction = this.registers.getRegister(Codes.PC) + Codes.INSTRUCTION_MEM_BEGIN;
                                    block = this.calculateInstructionBlock(this.registers.getRegister(Codes.PC));
                                    word = this.calculateWord(direction);
                                    position = block % 4;

                                    if (this.instructionCache.getTag(position) != block) {
                                        this.instructionCache.setBlock(position, this.instructionMemory.getBlock(direction));
                                        this.instructionCache.setTag(position, block);
                                    }

                                    Vector<Integer> instruction = this.instructionCache.getWord(position, word);

                                    // Freno por ahora
                                    if (instruction.toString().equals("[63, 0, 0, 0]")) {
                                        cycle = false;
                                    }

                                    System.out.println(instruction);
                                    //Se ejecuta una instruccion del hilillo
                                    this.instructions.decode(this.registers, instruction, this.dataMemory, this, this.cycleBarrier, this.clock);
                                    //Pasa un ciclo, se toma en cuenta para el quantum
                                    this.quantum--;

                                    System.out.println(block);
                                }
                                try {
                                    System.err.println("CORE " + this.coreId + ": BARRIER INSTRUCTION");
                                    cycleBarrier.await();
                                    this.clock++;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (BrokenBarrierException e) {
                                    e.printStackTrace();
                                }
                            }

                            this.results.set(fileID, this.registers);

                        } else {
                            this.semaphore.release();
                        }
                    }else{
                        this.semaphore.release();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } // Fin del while

            this.finished = true;

            do {
                try {
                    System.err.println("CORE " + this.coreId + ": BARRIER FINISH");
                    cycleBarrier.await();
                    this.clock++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    //e.printStackTrace();
                    System.err.println("Barrera reiniciada para que los cores terminen.");
                }
            }while (!this.otherCoreReference.getFinishedState());

        try {
            this.cycleBarrier.reset();
            System.err.println("CORE " + this.coreId + ": BARRIER GENERAL");
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private int calculateInstructionBlock(int direction){
        return direction / 16;
    }

    private int calculateWord(int direction){
        return (direction % 16) / 4;
    }
}
