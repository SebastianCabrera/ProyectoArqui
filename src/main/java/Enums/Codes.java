package Enums;

/**
 * Enum que contiene códigos especiales para ser usados en el programa.
 */
public enum Codes {

    ENUM;

    // Valores
    public static final int PC = 32;
    public static final int INSTRUCTION_MEM_BEGIN = 384;
    public static final String THREAD_1 = "Thread1";
    public static final String THREAD_2 = "Thread2";
    public static final int CORE_0 = 0;
    public static final int CORE_1 = 1;
    public static final int FAILURE = -1;
    public static final int SUCCESS = 0;

    // Códigos
    public static final int EMPTY_CACHE = 0;
    public static final int EMPTY_MEMORY = 1;
    public static final int EMPTY_REGISTER = 0;
    public static final int EMPTY_BLOCK = -1;

    // Codigos de takenfiles
    public static final int NOT_TAKEN = 0;
    public static final int IN_PROGRESS = 1;

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
    public static final int DATA_WORD_BYTES = 4;
    public static final int INSTRUCTIONS_WORD_SIZE = 4;
    public static final int TOTAL_INSTRUCTION_PARAMETERS = 4;
    public static final int DATA_BLOCK_BYTES = 16;
    public static final int BLOCK_BYTES = 16;

    // Instrucciones
    public static final int DADDI = 8;
    public static final int DADD = 32;
    public static final int DSUB = 34;
    public static final int DMUL = 12;
    public static final int DDIV = 14;
    public static final int BEQZ = 4;
    public static final int BNEZ = 5;
    public static final int JAL = 3;
    public static final int JR = 2;
    public static final int LW = 35;
    public static final int SW = 43;
    public static final int FIN = 63;
}
