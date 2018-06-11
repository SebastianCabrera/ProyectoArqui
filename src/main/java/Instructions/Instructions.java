package Instructions;

import Enums.Codes;

public class Instructions {
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
        registers[trgRegister] = registers[srcRegister1] / registers[srcRegister2];
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

    public void FIN() {

    }
}
