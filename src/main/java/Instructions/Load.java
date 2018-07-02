package Instructions;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class Load {

    private Vector<ReentrantLock> reservedStructures;

    public Load(){
        this.reservedStructures = new Vector<>();
    }

    // Retorna el dato en la palabra
    public int LW(int memDirection, DataMemory memory, Core currentCore, CyclicBarrier barrier){

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
        // si es la misma etiqueta y el estado no es I (es C o M)
        else if(state != Codes.I){
            return this.finishLW(memDirection, currentCore, position);
        }

        // Si es la mimsa etiqueta pero en I, sigue desde aquí

        // Si no se puede bloquear la otra caché
        if(!(currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().tryLock())){
            return this.restart();
        }

        // Si no está reservada sigue desde aquí y se bloquea
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
        if(otherCacheState == Codes.M){
            // Guardar bloque en memoria

            // Si no se puede bloquear la memoria
            if(!(memory.getMemoryBusLock().tryLock())){
                return this.restart();
            }

            // Si no está reservada, sigue aquí y se bloquea
            memory.setBlock(currentCore.getMemoryDirectionPosition(currentCore.getBlockBegin(otherCacheTag * 16)), currentCore.getOtherCoreReference().getDataCache().getBlock(otherCachePosition));
            currentCore.getOtherCoreReference().getDataCache().setState(otherCachePosition, Codes.I);

            memory.getMemoryBusLock().unlock();
        }

        // Cargar bloque en caché propia (40 ciclos, controlar con barrera).
        int j = 0;
        while(j<40)
        {
            try {
                System.err.println("CORE " + currentCore.getCoreId() + ": BARRIER LOAD");
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
        currentCore.getDataCache().setState(position, Codes.C);
        currentCore.getDataCache().setTag(position, block);

        // Liberar
        currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition).unlock();
        currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().unlock();

        return this.finishLW(memDirection, currentCore, position);
    }

    private int finishLW(int memDirection, Core currentCore, int position){
        int value = currentCore.getDataCache().getWord(position, currentCore.calculateDataWordPosition(memDirection));
        currentCore.getDataCache().getPositionLock(position).unlock();
        this.reservedStructures.clear();

        return value;
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
