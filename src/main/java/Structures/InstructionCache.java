package Structures;

import Abstracts.Cache;
import Enums.Codes;

public class InstructionCache extends Cache{

    public InstructionCache(int totalBlocks) {
        super(totalBlocks, Codes.INSTRUCTIONS_WORD_SIZE);
    }

}
