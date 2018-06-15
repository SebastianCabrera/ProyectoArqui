package Instructions;

import Enums.Codes;

public class Instructions {
    public void decode(int[] registers, String line)
    {
        String[] splitLine = line.split(" ");
        int value0 = Integer.parseInt(splitLine[0]);
        int value1 = Integer.parseInt(splitLine[1]);
        int value2 = Integer.parseInt(splitLine[2]);
        int value3 = Integer.parseInt(splitLine[0]);
        switch(value0)
        {
            case 8:
                DADDI(registers, value2, value1, value3);
                break;
            case 32:
                DADD(registers, value2, value1, value3);
                break;
            case 34:
                DSUB(registers, value2, value1, value3);
                break;
            case 12:
                DMUL(registers, value2, value1, value3);
                break;
            case 14:
                DDIV(registers, value2, value1, value3);
                break;
            case 4:
                BEQZ(registers, value1, value3);
                break;
            case 5:
                BNEZ(registers, value1, value3);
                break;
            case 3:
                JAL(registers, value3);
                break;
            case 2:
                JR(registers, value1);
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

    public void FIN() {
        //mata todo
    }
}
