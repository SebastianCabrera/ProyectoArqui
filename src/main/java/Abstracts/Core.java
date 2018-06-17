package Abstracts;

import Enums.Codes;
import Structures.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Core {
    DataMemory dataMemory;
    InstructionMemory instructionMemory;

    DataCache dataCache;
    InstructionCache instructionCache;

    Registers registers;

    protected Core(int cacheSize){
        this.dataMemory = new DataMemory();
        this.instructionMemory = new InstructionMemory();

        this.dataCache = new DataCache(cacheSize);
        this.instructionCache = new InstructionCache(cacheSize);

        this.registers = new Registers();
    }

    //Recibe la direccion del archivo con los hilillos
    static InstructionMemory readHilillos(String hilillosDirection) {

        InstructionMemory insMem = new InstructionMemory();
        /*File encyptFile = new File(hilillosDirection);
        if (encyptFile.isFile()) {
            String line;
            BufferedReader in;

            try {
                in = new BufferedReader(new FileReader(encyptFile));
                try {
                    line = in.readLine();

                    while (line != null) {
                        //Guardar una por una en insMem (FALTA)

                        line = in.readLine();
                    }
                } catch (IOException l) {
                    l.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }*/

        return insMem;
    }
}
