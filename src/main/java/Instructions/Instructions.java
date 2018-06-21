package Instructions;

import Enums.Codes;

import java.util.Vector;

// IR: Instruccion Actual

// Doble barrera: por el doble hilillo, cuando se espera que un hilo acomode (contexto, subir y demas), los otros no
// estan haciendo nada. El SO se encarga de esto.

// En tal ciclo todos hicieron lo que les tocara. Antes de comenzar el otro (segunda barrera): si a alguien
// se le acaba el quantum se sube el siguiente, si da fallo se detiene, y una vez se hizo toda esa revision
// (de parte de solo un hilo o cada uno) en la primera barrera, luego de la segunda ya ejecutan el ciclo normal.
// Seria en medio de ambas barreras. No es obligatorio.

public class Instructions {
    public void decode(int[] registers, Vector<Integer> word)
    {
        switch(word.get(0))
        {
            case 8:
                DADDI(registers, word.get(2), word.get(1), word.get(3));
                break;
            case 32:
                DADD(registers, word.get(2), word.get(1), word.get(3));
                break;
            case 34:
                DSUB(registers, word.get(2), word.get(1), word.get(3));
                break;
            case 12:
                DMUL(registers, word.get(2), word.get(1), word.get(3));
                break;
            case 14:
                DDIV(registers, word.get(2), word.get(1), word.get(3));
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
                //Llama a la clase LOAD
                break;
            case 43:
                //Llama a la clase STORE
                break;
            case 63:
                FIN();
                break;
        }
    }


    public void DADDI(int[] registers, int trgRegister, int srcRegister, int inm) {
        registers[trgRegister] = registers[srcRegister] + inm;
    }

    public void DADD(int[] registers, int trgRegister, int srcRegister1, int srcRegister2) {
        registers[trgRegister] = registers[srcRegister1] + registers[srcRegister2];
    }

    public void DSUB(int[] registers, int trgRegister, int srcRegister1, int srcRegister2) {
        registers[trgRegister] = registers[srcRegister1] - registers[srcRegister2];
    }

    public void DMUL(int[] registers, int trgRegister, int srcRegister1, int srcRegister2) {
        registers[trgRegister] = registers[srcRegister1] * registers[srcRegister2];
    }

    public void DDIV(int[] registers, int trgRegister, int srcRegister1, int srcRegister2) {
        if(registers[srcRegister2] != 0) {
            registers[trgRegister] = registers[srcRegister1] / registers[srcRegister2];
        }
    }

    public void BEQZ(int[] registers, int srcRegister, int imn) {
        if(registers[srcRegister] == 0){
            registers[Codes.PC] += (4 * imn);
        }
    }

    public void BNEZ(int[] registers, int srcRegister, int imn) {
        if(registers[srcRegister] != 0){
            registers[Codes.PC] += (4 * imn);
        }
    }

    public void JAL(int[] registers, int imn) {
        registers[31] = registers[Codes.PC];
        registers[Codes.PC] += imn;
    }

    public void JR(int[] registers, int srcRegister) {
        registers[Codes.PC] = registers[srcRegister];
    }

    public int FIN() {
        return Codes.END;
    }
}
