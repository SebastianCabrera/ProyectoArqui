package Instructions;

import Cores.SingleCore;
import Enums.Codes;
import Structures.DataMemory;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene la lógica de la instrucción LOAD
 */
public class Load {

    // Vector con referencias a las estructuras reservadas por candados.
    private Vector<ReentrantLock> reservedStructures;

    /**
     * Constructor de la clase
     */
    public Load(){
        this.reservedStructures = new Vector<>();
    }

    /**
     * Instrucción LW.
     * @param memDirection Dirección de memoria a acceder
     * @param memory Referencia a la memoria de datos
     * @param currentCore Referencia al núcleo que ejecuta la instrucción
     * @return El valor a almacenar en el registro destino o código de error
     */
    public int LW(int memDirection, DataMemory memory, SingleCore currentCore){

        // Obtiene número de bloque
        int block = currentCore.calculateDataBlockNumber(memDirection);

        // Obtiene número de posición en caché
        int position = currentCore.calculateCachePosition(block, currentCore.getCoreId());

        // Si la posición está reservada, reinicia
        if(!(currentCore.getDataCache().getPositionLock(position).tryLock())){
            return this.restart();
        }

        // Si sigue aquí, es que no está reservada y se bloqueó.
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

        // Cargar bloque en caché propia (39 ciclos + ciclo externo en SingleCore).
        currentCore.waitFortyCycles();

        currentCore.getDataCache().setBlock(position, memory.getBlock(currentCore.getMemoryDirectionPosition(currentCore.getBlockBegin(memDirection))));
        currentCore.getDataCache().setState(position, Codes.C);
        currentCore.getDataCache().setTag(position, block);

        // Liberar
        currentCore.getOtherCoreReference().getDataCache().getPositionLock(otherCachePosition).unlock();
        currentCore.getOtherCoreReference().getDataCache().getCacheBusLock().unlock();

        return this.finishLW(memDirection, currentCore, position);
    }

    /**
     * Método auxiliar que maneja el fin del LW
     * @param memDirection Dirección de memoria a acceder
     * @param currentCore Referencia al núcleo que ejecuta la instrucción
     * @param position Posición que está siendo usada en la caché
     * @return El valor a almacenar en el registro destino o código de error
     */
    private int finishLW(int memDirection, SingleCore currentCore, int position){
        int value = currentCore.getDataCache().getWord(position, currentCore.calculateDataWordPosition(memDirection));
        currentCore.getDataCache().getPositionLock(position).unlock();
        this.reservedStructures.clear();

        return value;
    }

    /**
     * En caso de no obtener algún candado, se liberan todos.
     * @return Código de error.
     */
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
