package Instructions;

import Abstracts.Core;
import Abstracts.InstructionsResources;
import Enums.Codes;
import Structures.DataMemory;

public class Store extends InstructionsResources{
    public Store(){
        super();
    }

    public int SW(int memDirection, DataMemory memory, Core currentCore, int registerValue){
        // Obtiene número de bloque
        int block = this.calculateBlock(memDirection);

        // Obtiene número de posición en caché
        int position = this.calculateCachePosition(block, currentCore.getCoreId());

        // Si la posición está reservada, reinicia
        if(!(currentCore.getDataCache().getPositionLock(position).tryLock())){
            this.restart();
            System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
            System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
            System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
            System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
            System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
            System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
            System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
            System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
            System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
            System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return -1;
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
                    this.restart();
                    System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
                    System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
                    System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
                    System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
                    System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
                    System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
                    System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
                    System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
                    System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
                    System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return -1;
                }
                // Si sigue aquí, es porque no estaba reservada y se bloque'o

                // Guardar bloque de la caché en memoria.
                memory.setBlock(tag * 16, currentCore.getDataCache().getBlock(position));
                currentCore.getDataCache().setSate(position, Codes.I);

                memory.getMemoryBusLock().unlock();
            }
        }
        // Si el estado es M, termina
        if(state == Codes.M){
            return this.finishSW(memDirection, currentCore, position, registerValue);
        }

        // Si no puede bloquear el bus a la otra cache, reinicia.
        if(!(currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().tryLock())){
            this.restart();
            System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
            System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
            System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
            System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
            System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
            System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
            System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
            System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
            System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
            System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return -1;
        }

        // Si no esta reservado, continua desde aqui ya bloqueado
        this.reservedStructures.add(currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());

        // Calcular columna en la otra caché
        int otherCachePosition = this.calculateCachePosition(block, currentCore.getOtherCoreReference().getCoreId());

        // Si no se puede bloquear la posición de la otra caché
        if(!(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition)).tryLock()){
            this.restart();
            System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
            System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
            System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
            System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
            System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
            System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
            System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
            System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
            System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
            System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return -1;
        }

        // Si no está reservada continúa desde aquí y se bloquea
        this.reservedStructures.add(currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition));

        // Obtener estado en la otra caché
        char otherCacheState = currentCore.getOtherCoreReference().getDataCache().getState(otherCachePosition);

        // Obtener etiqueta en otherCache
        int otherCacheTag = currentCore.getDataCache().getTag(position);

        System.out.println("OTHERCACHE TAG: " + otherCacheTag);
        System.out.println("OTHERCACHEPOS: " + otherCachePosition);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Si el estado en la otra caché es M
        if(otherCacheState == Codes.M) {
            // Guardar bloque en memoria

            // Si no se puede bloquear la memoria
            if (!(memory.getMemoryBusLock().tryLock())) {
                this.restart();
                System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
                System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
                System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
                System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
                System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
                System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
                System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
                System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
                System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
                System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return -1;
            }

            // Si no está reservada, sigue aquí ya bloqueada

            // Guardar bloque de la otra cache en memoria

            memory.setBlock(otherCacheTag * 16, currentCore.getOtherCoreReference().getDataCache().getBlock(otherCachePosition));
            memory.getMemoryBusLock().unlock();

            // Invalidar posicion
            currentCore.getOtherCoreReference().getDataCache().setSate(otherCachePosition, Codes.I);

            // Si el estado de la otra cache es C
        }else if(otherCacheState == Codes.C){
            // Invalidar posicion
            currentCore.getOtherCoreReference().getDataCache().setSate(otherCachePosition, Codes.I);
        }

        // Liberar posicion de la otra cache y el bus hacia la mimsma
        currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition).unlock();
        currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().unlock();

        // Si el estado en mi cache no es C
        if(state != Codes.C){
            // Si no se puede bloquear memoria
            if(!(memory.getMemoryBusLock().tryLock())){
                this.restart();
                System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
                System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
                System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
                System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
                System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
                System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
                System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
                System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
                System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
                System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return -1;
            }

            // Cargar bloque en caché propia (40 ciclos, controlar con barrera).
            currentCore.getDataCache().setBlock(position, memory.getBlock(memDirection));

            // Liberar memoria
            memory.getMemoryBusLock().unlock();
        }

        // Poner posicion en M
        currentCore.getDataCache().setSate(position, Codes.M);
        currentCore.getDataCache().setTag(position, block);

        return this.finishSW(memDirection, currentCore, position, registerValue);
    }

    private int finishSW(int memDirection, Core currentCore, int position, int registerValue){
        currentCore.getDataCache().setWord(position, this.calculateWord(memDirection), registerValue);
        currentCore.getDataCache().getPositionLock(position).unlock();

        System.out.println("POS 0 MYCACHE " + currentCore.getDataCache().getPositionLock(0));
        System.out.println("POS 1 MYCACHE " + currentCore.getDataCache().getPositionLock(1));
        System.out.println("POS 2 MYCACHE " + currentCore.getDataCache().getPositionLock(2));
        System.out.println("POS 3 MYCACHE " + currentCore.getDataCache().getPositionLock(3));
        System.out.println("MYCACHE " + currentCore.getDataCache().getCacheBusLock());
        System.out.println("POS 0 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(0));
        System.out.println("POS 1 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(1));
        System.out.println("POS 2 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(2));
        System.out.println("POS 3 OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getPositionLock(3));
        System.out.println("OTHERCACHE " + currentCore.getOtherCoreReference().getDataCache().getCacheBusLock());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.reservedStructures.clear();

        return Codes.SUCCESS;
    }
}
