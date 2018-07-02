package Instructions;

import Cores.SingleCore;
import Enums.Codes;
import Structures.DataMemory;
import Structures.Registers;

import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

/**
 * Clase que contiene todas las instrucciones para ser ejecutadas por el núcleo
 */
public class Instructions {

    private Load load;      // Instancia de la instrucción Load
    private Store store;    // Instancia de la instrucción Store

    /**
     * Constructor de la clase de instrucciones.
     */
    public Instructions(){
        this.load = new Load();
        this.store = new Store();
    }

    /**
     * Método que permite decodificar y ejecutar una instrucción
     * @param registers Los referencia a los registros en los cuales se realizará la opreación
     * @param instruction La palabra de instrucciones a ejecutar
     * @param memory La referencia a la memoria de datos del sistema
     * @param currentCore La referencia al núcleo que ejecuta la instrucción
     * @param barrier La referencia a la barrera de los ciclos para ser eventualmente usada por Load y Store
     */
    public void decode(Registers registers, Vector<Integer> instruction, DataMemory memory, SingleCore currentCore, CyclicBarrier barrier)
    {
        // Valor de retorno de Load o Store
        int value;

        switch(instruction.get(0)) {
            case Codes.DADDI:
                DADDI(registers, instruction.get(2), instruction.get(1), instruction.get(3));
                break;
            case Codes.DADD:
                DADD(registers, instruction.get(1), instruction.get(2), instruction.get(3));
                break;
            case Codes.DSUB:
                DSUB(registers, instruction.get(1), instruction.get(2), instruction.get(3));
                break;
            case Codes.DMUL:
                DMUL(registers, instruction.get(1), instruction.get(2), instruction.get(3));
                break;
            case Codes.DDIV:
                DDIV(registers, instruction.get(1), instruction.get(2), instruction.get(3));
                break;
            case Codes.BEQZ:
                BEQZ(registers, instruction.get(1), instruction.get(3));
                break;
            case Codes.BNEZ:
                BNEZ(registers, instruction.get(1), instruction.get(3));
                break;
            case Codes.JAL:
                JAL(registers, instruction.get(3));
                break;
            case Codes.JR:
                JR(registers, instruction.get(1));
                break;
            case Codes.LW:
                value = load.LW((instruction.get(3) + registers.getRegister(instruction.get(1))), memory, currentCore, barrier);
                if(value != Codes.FAILURE){
                    registers.setRegister(instruction.get(2), value);
                }else{
                    // En caso de no haber podido terminar, regresa el PC para volver a ejecutar el LW.
                    registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) - 4);
                }
                break;
            case Codes.SW:
                value = store.SW((instruction.get(3) + registers.getRegister(instruction.get(1))), memory, currentCore, registers.getRegister(instruction.get(2)), barrier);
                if(value == Codes.FAILURE){
                    // En caso de no haber podido terminar, regresa el PC para volver a ejecutar el SW.
                    registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) - 4);
                }
                break;
            case Codes.FIN:
                FIN();
                break;
        }

        registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + 4);
    }

    /**
     * Instrucción DADDI
     * @param registers Referencia a los registros.
     * @param trgRegister Registro destino.
     * @param srcRegister Registro fuente.
     * @param imn Valor inmediato.
     */
    private void DADDI(Registers registers, int trgRegister, int srcRegister, int imn) {
        registers.setRegister(trgRegister, registers.getRegister(srcRegister) + imn);
    }

    /**
     * Instrucción DADD
     * @param registers Referencia a los registros.
     * @param srcRegister1 Registro fuente 1.
     * @param srcRegister2 Registro fuente 2.
     * @param trgRegister Registro destino.
     */
    private void DADD(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) + registers.getRegister(srcRegister2)));
    }

    /**
     * Instrucción DSUB
     * @param registers Referencia a los registros.
     * @param srcRegister1 Registro fuente 1.
     * @param srcRegister2 Registro fuente 2.
     * @param trgRegister Registro destino.
     */
    private void DSUB(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) - registers.getRegister(srcRegister2)));
    }

    /**
     * Instrucción DMUL
     * @param registers Referencia a los registros.
     * @param srcRegister1 Registro fuente 1.
     * @param srcRegister2 Registro fuente 2.
     * @param trgRegister Registro destino.
     */
    private void DMUL(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) * registers.getRegister(srcRegister2)));
    }

    /**
     * Instrucción DDIV
     * @param registers Referencia a los registros.
     * @param srcRegister1 Registro fuente 1.
     * @param srcRegister2 Registro fuente 2.
     * @param trgRegister Registro destino.
     */
    private void DDIV(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        if(registers.getRegister(srcRegister2) != 0) {
            registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) / registers.getRegister(srcRegister2)));
        }
    }

    /**
     * Instrucción BEQZ
     * @param registers Referencia a los registros.
     * @param srcRegister Registro fuente.
     * @param imn Valor inmediato.
     */
    private void BEQZ(Registers registers, int srcRegister, int imn) {
        if(registers.getRegister(srcRegister) == 0){
            registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + (4 * imn));
        }
    }

    /**
     * Instrucción BNEZ
     * @param registers Referencia a los registros.
     * @param srcRegister Registro fuente.
     * @param imn Valor inmediato.
     */
    private void BNEZ(Registers registers, int srcRegister, int imn) {
        if(registers.getRegister(srcRegister) != 0){
            registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + (4 * imn));
        }
    }

    /**
     * Instrucción JAL
     * @param registers Referencia a los registros.
     * @param imn Valor inmediato.
     */
    private void JAL(Registers registers, int imn) {
        registers.setRegister(31, registers.getRegister(Codes.PC)+4);
        registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + imn);
    }

    /**
     * Instrucción JR
     * @param registers Referencia a los registros.
     * @param srcRegister Registro fuente.
     */
    private void JR(Registers registers, int srcRegister) {
        registers.setRegister(Codes.PC, registers.getRegister(srcRegister) - 4);
    }

    /**
     * Instrucción fin
     */
    private void FIN() {

    }
}
