package Instructions;

import Abstracts.Core;
import Abstracts.InstructionsResources;
import Enums.Codes;
import Structures.DataMemory;

public class Load extends InstructionsResources{

    public Load(){
        super();
    }

    // Retorna el dato en la palabra
    public int LW(int memDirection, DataMemory memory, Core currentCore){

        // Obtiene número de bloque
        int block = this.calculateBlock(memDirection);

        // Obtiene número de posición en caché
        int position = this.calculateCachePosition(block, currentCore.getCoreId());

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
                memory.setBlock(tag * 16, currentCore.getDataCache().getBlock(position));
                currentCore.getDataCache().setSate(position, Codes.I);

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
        int otherCachePosition = this.calculateCachePosition(block, currentCore.getOtherCoreReference().getCoreId());

        // Si no se puede bloquear la posición de la otra caché
        if(!(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition)).tryLock()){
            return this.restart();
        }

        // Si no está reservada continúa desde aquí y se bloquea
        this.reservedStructures.add(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition));

        // Obtener estado en la otra caché
        char otherCacheState = currentCore.getOtherCoreReference().getDataCache().getState(otherCachePosition);

        // Obtener etiqueta en otherCache
        int otherCacheTag = currentCore.getOtherCoreReference().getDataCache().getTag(position);

        // Si el estado en la otra caché es M
        if(otherCacheState == Codes.M){
            // Guardar bloque en memoria

            // Si no se puede bloquear la memoria
            if(!(memory.getMemoryBusLock().tryLock())){
                return this.restart();
            }

            // Si no está reservada, sigue aquí y se bloquea
            memory.setBlock(otherCacheTag * 16, currentCore.getOtherCoreReference().getDataCache().getBlock(otherCachePosition));
            currentCore.getOtherCoreReference().getDataCache().setSate(otherCachePosition, Codes.I);

            memory.getMemoryBusLock().unlock();
        }

        // Cargar bloque en caché propia (40 ciclos, controlar con barrera).
        currentCore.getDataCache().setBlock(position, memory.getBlock(memDirection));
        currentCore.getDataCache().setSate(position, Codes.C);
        currentCore.getDataCache().setTag(position, block);

        // Liberar
        currentCore.getOtherCoreReference().getDataCache().getPositionLock(position).unlock();
        currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().unlock();

        return this.finishLW(memDirection, currentCore, position);
    }

    private int finishLW(int memDirection, Core currentCore, int position){
        int value = currentCore.getDataCache().getWord(position, this.calculateWord(memDirection));
        currentCore.getDataCache().getPositionLock(position).unlock();
        this.reservedStructures.clear();

        return value;
    }
}
