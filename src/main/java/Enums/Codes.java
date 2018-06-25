package Enums;

public enum Codes {

    ENUM;

    // Valores
    public static final int PC = 33;
    public static final int DATA_MEM_BEGIN = 0;
    public static final int DATA_MEM_END = 380;
    public static final int INSTRUCTION_MEM_BEGIN = 384;
    public static final int INSTRUCTION_MEM_END = 10240;
    public static final int END = -1;
    public static final String THREAD_0 = "Thread0";
    public static final String THREAD_1 = "Thread1";
    public static final String THREAD_2 = "Thread2";
    public static final int CORE_0 = 0;
    public static final int CORE_1 = 1;
    public static final int FAILURE = -1;

    // Códigos
    public static final int EMPTY_CACHE = 0;
    public static final int EMPTY_MEMORY = 0;
    public static final int EMPTY_REGISTER = 0;

    // Etiquetas
    public static final char I = 'I';
    public static final char M = 'M';
    public static final char C = 'C';

    // Tamaños
    public static final int TOTAL_REGISTERS = 33;
    public static final int DATA_MEM_CAPACITY = 96;
    public static final int INSTRUCTION_MEM_CAPACITY = 640;
    public static final int CACHE_BLOCK_SIZE = 4;
    public static final int BLOCKS_IN_CACHE_0 = 8;
    public static final int BLOCKS_IN_CACHE_1 = 4;
    public static final int DATA_WORD_SIZE = 1;
    public static final int DATA_WORD_BYTES = 4;
    public static final int INSTRUCTIONS_WORD_BYTES = 16;
    public static final int INSTRUCTIONS_WORD_SIZE = 4;
    public static final int TOTAL_INSTRUCTION_PARAMETERS = 4;
    public static final int TOTAL_CORES = 3;
    public static final int DATA_BLOCK_BYTES = 16;
}
