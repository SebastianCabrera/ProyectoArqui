package Instructions;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class Store {
    private Vector<ReentrantLock> reservedStructures;

    public Store(){
        this.reservedStructures = new Vector<>();
    }

    public int SW(int memDirection, DataMemory memory, Core currentCore, int registerValue, CyclicBarrier barrier){
        // Obtiene número de bloque
        int block = currentCore.calculateDataBlockNumber(memDirection);

        // Obtiene número de posición en caché
        int position = currentCore.calculateCachePosition(block, currentCore.getCoreId());

        // Si la posición está reservada, reinicia
        if(!(currentCore.getDataCache().getPositionLock(position).tryLock())){
            return this.restart();
        }

        // Si sigue aquí, es que on está reservada y se bloqueó.
        this.reservedStructures.add(currentCore.getDataCache().getPositionLock(position));

        // Obtener estado en myCache
        char state = currentCore.getDataCache().getState(position);

        // Obtener etiqueta en myCache
        int tag = currentCore.getDataCache().getTag(position);

        // Si la etiqueta es diferente
        if(tag != block){

            // Si el estado es M
            if(state == Codes.M){

                // Si no se puede bloquear bus a memoria
                if(!(memory.getMemoryBusLock().tryLock())){
                    return this.restart();
                }
                // Si sigue aquí, es porque no estaba reservada y se bloque'o

                // Guardar bloque de la caché en memoria.
                memory.setBlock(currentCore.getMemoryDirectionPosition(currentCore.getBlockBegin(tag * 16)), currentCore.getDataCache().getBlock(position));
                currentCore.getDataCache().setState(position, Codes.I);
                state = Codes.I;

                memory.getMemoryBusLock().unlock();
            }
        }
        // Si el estado es M, termina
        if(state == Codes.M){
            return this.finishSW(memDirection, currentCore, position, registerValue);
        }

        // Si no puede bloquear el bus a la otra cache, reinicia.
        if(!(currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().tryLock())){
            return this.restart();
        }

        // Si no esta reservado, continua desde aqui ya bloqueado
        this.reservedStructures.add(currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());

        // Calcular columna en la otra caché
        int otherCachePosition = currentCore.calculateCachePosition(block, currentCore.getOtherCoreReference().getCoreId());

        // Si no se puede bloquear la posición de la otra caché
        if(!(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition)).tryLock()){
            return this.restart();
        }

        // Si no está reservada continúa desde aquí y se bloquea
        this.reservedStructures.add(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition));

        // Obtener estado en la otra caché
        char otherCacheState = currentCore.getOtherCoreReference().getDataCache().getState(otherCachePosition);

        // Obtener etiqueta en otherCache
        int otherCacheTag = currentCore.getOtherCoreReference().getDataCache().getTag(otherCachePosition);

        // Si el estado en la otra caché es M
        if(otherCacheState == Codes.M) {
            // Guardar bloque en memoria

            // Si no se puede bloquear la memoria
            if (!(memory.getMemoryBusLock().tryLock())) {
                return this.restart();
            }

            // Si no está reservada, sigue aquí ya bloqueada

            // Guardar bloque de la otra cache en memoria
            memory.setBlock(currentCore.getMemoryDirectionPosition(currentCore.getBlockBegin(otherCacheTag * 16)), currentCore.getOtherCoreReference().getDataCache().getBlock(otherCachePosition));
            memory.getMemoryBusLock().unlock();

            // Invalidar posicion
            currentCore.getOtherCoreReference().getDataCache().setState(otherCachePosition, Codes.I);

            // Si el estado de la otra cache es C
        }else if(otherCacheState == Codes.C){
            // Invalidar posicion
            currentCore.getOtherCoreReference().getDataCache().setState(otherCachePosition, Codes.I);
        }

        // Liberar posicion de la otra cache y el bus hacia la mimsma
        currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition).unlock();
        currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().unlock();

        // Si el estado en mi cache no es C, o es C pero no es el que busco
        if((state != Codes.C) || (state == Codes.C && tag != block)){
            // Si no se puede bloquear memoria
            if(!(memory.getMemoryBusLock().tryLock())){
                return this.restart();
            }

            // Cargar bloque en caché propia (40 ciclos, controlar con barrera).
            int j = 0;
            while(j<40)
            {
                try {
                    System.err.println("CORE " + currentCore.getCoreId() + ": BARRIER STORE");
                    barrier.await();
                    currentCore.addToClock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                j++;
            }
            currentCore.getDataCache().setBlock(position, memory.getBlock(currentCore.getMemoryDirectionPosition(currentCore.getBlockBegin(memDirection))));


            // Liberar memoria
            memory.getMemoryBusLock().unlock();
        }

        // Poner posicion en M
        currentCore.getDataCache().setState(position, Codes.M);
        currentCore.getDataCache().setTag(position, block);

        return this.finishSW(memDirection, currentCore, position, registerValue);
    }

    private int finishSW(int memDirection, Core currentCore, int position, int registerValue){
        currentCore.getDataCache().setWord(position, currentCore.calculateDataWordPosition(memDirection), registerValue);
        currentCore.getDataCache().getPositionLock(position).unlock();

        this.reservedStructures.clear();

        return Codes.SUCCESS;
    }

    private int restart(){
        for(int i = 0; i < this.reservedStructures.size(); i++){
            if(this.reservedStructures.get(i).isLocked()) {
                this.reservedStructures.get(i).unlock();
            }
        }
        this.reservedStructures.clear();

        return Codes.FAILURE;
    }
}
