package Structures;

import Abstracts.Cache;
import Enums.Codes;

import java.util.Vector;

public class DataCache extends Cache {

    public DataCache(int totalBlocks) {
        super(totalBlocks, Codes.DATA_WORDS_SIZE);
    }
}
