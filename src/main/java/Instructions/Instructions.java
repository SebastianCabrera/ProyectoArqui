package Instructions;

import Abstracts.Core;
import Enums.Codes;
import Structures.DataMemory;
import Structures.Registers;

import java.util.Vector;

import static java.lang.System.exit;

// IR: Instruccion Actual

// Doble barrera: por el doble hilillo, cuando se espera que un hilo acomode (contexto, subir y demas), los otros no
// estan haciendo nada. El SO se encarga de esto.

// En tal ciclo todos hicieron lo que les tocara. Antes de comenzar el otro (segunda barrera): si a alguien
// se le acaba el quantum se sube el siguiente, si da fallo se detiene, y una vez se hizo toda esa revision
// (de parte de solo un hilo o cada uno) en la primera barrera, luego de la segunda ya ejecutan el ciclo normal.
// Seria en medio de ambas barreras. No es obligatorio.

public class Instructions {

    Load load;
    Store store;

    public Instructions(){
        this.load = new Load();
        this.store = new Store();
    }

    public void decode(Registers registers, Vector<Integer> word, DataMemory memory, Core currentCore)
    {
        int value;
        switch(word.get(0)) {
            case 8:
                DADDI(registers, word.get(2), word.get(1), word.get(3));
                break;
            case 32:
                DADD(registers, word.get(1), word.get(2), word.get(3));
                break;
            case 34:
                DSUB(registers, word.get(1), word.get(2), word.get(3));
                break;
            case 12:
                DMUL(registers, word.get(1), word.get(2), word.get(3));
                break;
            case 14:
                DDIV(registers, word.get(1), word.get(2), word.get(3));
                break;
            case 4:
                BEQZ(registers, word.get(1), word.get(3));
                break;
            case 5:
                BNEZ(registers, word.get(1), word.get(3));
                break;
            case 3:
                JAL(registers, word.get(3));
                break;
            case 2:
                JR(registers, word.get(1));
                break;
            case 35:
                value = load.LW((word.get(3) + registers.getRegister(word.get(1))), memory, currentCore);
                if(value != Codes.FAILURE){
                    registers.setRegister(word.get(2), value);
                }else{
                    registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) - 4);
                }
                break;
            case 43:
                value = store.SW((word.get(3) + registers.getRegister(word.get(1))), memory, currentCore, registers.getRegister(word.get(2)));
                if(value == Codes.FAILURE){
                    registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) - 4);
                }
                break;
            case 63:
                FIN();
                // Esto es temporal. Se debe agregar en otra parte
                Registers context = new Registers(registers);

                currentCore.addContext(context);
                for(int i = 0; i < 32; i++){
                    registers.setRegister(i, Codes.EMPTY_REGISTER);
                }
                //exit(0);
                break;
        }

        System.out.println("Registers");

        int index = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 4; j++) {
                System.out.print(registers.getRegister(index) + " ");
                index++;
            }
            System.out.println();
        }

        registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + 4);
    }


    public void DADDI(Registers registers, int trgRegister, int srcRegister, int inm) {
        registers.setRegister(trgRegister, registers.getRegister(srcRegister) + inm);
    }

    public void DADD(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) + registers.getRegister(srcRegister2)));
    }

    public void DSUB(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) - registers.getRegister(srcRegister2)));
    }

    public void DMUL(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) * registers.getRegister(srcRegister2)));
    }

    public void DDIV(Registers registers, int srcRegister1, int srcRegister2, int trgRegister) {
        if(registers.getRegister(srcRegister2) != 0) {
            registers.setRegister(trgRegister, (registers.getRegister(srcRegister1) / registers.getRegister(srcRegister2)));
        }
    }

    public void BEQZ(Registers registers, int srcRegister, int imn) {
        if(registers.getRegister(srcRegister) == 0){
            registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + (4 * imn));
        }
    }

    public void BNEZ(Registers registers, int srcRegister, int imn) {
        if(registers.getRegister(srcRegister) != 0){
            registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + (4 * imn));
        }
    }

    public void JAL(Registers registers, int imn) {
        registers.setRegister(31, registers.getRegister(Codes.PC)+4);
        registers.setRegister(Codes.PC, registers.getRegister(Codes.PC) + imn);
    }

    public void JR(Registers registers, int srcRegister) {
        registers.setRegister(Codes.PC, registers.getRegister(srcRegister) - 4);
    }

    public int FIN() {
        return Codes.END;
    }
}
