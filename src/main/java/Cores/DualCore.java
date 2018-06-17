package Cores;

import Abstracts.Core;
import Enums.Codes;
import Structures.Registers;

import java.util.Vector;

/**
 * Created by J.A Rodríguez on 16/06/2018.
 */
public class DualCore extends Core {
    Vector<Registers> contextsList;

    public DualCore(){
        super(Codes.BLOCKS_IN_CACHE_0);
        contextsList = new Vector<>();
    }
}
