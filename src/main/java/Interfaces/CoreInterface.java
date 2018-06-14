package Interfaces;

import Structures.InstructionMemory;

import java.io.*;

public interface CoreInterface {

    //Recibe la direccion del archivo con los hilillos
    static InstructionMemory readHilillos(String hilillosDirection)
    {
        InstructionMemory insMem = new InstructionMemory();
        File encyptFile=new File(hilillosDirection);
        if(encyptFile.isFile())
        {
            String line;
            BufferedReader in;

            try
            {
                in = new BufferedReader(new FileReader(encyptFile));
                try
                {
                    line = in.readLine();

                    while(line != null)
                    {
                        //Guardar una por una en insMem (FALTA)

                        line = in.readLine();
                    }
                }
                catch (IOException l)
                {
                    l.printStackTrace();
                }
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }

        return insMem;
    }
}
