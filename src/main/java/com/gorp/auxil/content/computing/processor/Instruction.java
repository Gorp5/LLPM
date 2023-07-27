package com.gorp.auxil.content.computing.processor;

public enum Instruction {
    
    /* [CODE] + [OPERAND 0] + [ADDRESSING MODE 1] + [OPERAND 1] + ..... + [ADDRESSING MODE N] + [OPERAND N]
     Addressing Modes:
        0x0 - Immediate
        0x1 - Register
        0x3 - Register Indirect
     */
    
    NOOP((byte)             0x0,    0),
    
    // Arithmetic
    ADD((byte)              0x1,    4), // 3 Addressing Modes
    SUBTRACT((byte)         0x2,    4), // 3 Addressing Modes
    MULTIPLY((byte)         0x3,    4), // 3 Addressing Modes
    DIVIDE((byte)           0x4,    4), // 3 Addressing Modes
    
    // Logic
    AND((byte)              0x5,    3), // 3 Addressing Modes
    OR((byte)               0x6,    3), // 3 Addressing Modes
    XOR((byte)              0x7,    3), // 3 Addressing Modes
    
    // Tests
    BIT_TEST((byte)         0x8,    3), // 3 Addressing Modes
    
    // Control Flow
    BRANCH_IF((byte)        0x9,    4), // 3 Addressing Modes
    JUMP((byte)             0xa,    4), // 3 Addressing Modes
    CALL_SUBROUTINE((byte)  0xb,    4), // 3 Addressing Modes
    RETURN_SUBROUTINE((byte)0xc,    0),
    
    // Bitwise
    BIT_SET((byte)          0xd,    5), // 3 Addressing Modes
    BIT_SHIFT_LEFT((byte)   0xe,    4), // 3 Addressing Modes
    BIT_SHIFT_RIGHT((byte)  0xf,    4), // 3 Addressing Modes
    
    // Data Transfer
    LOAD((byte)             0xf1,    2), // 3 Addressing Modes
    STORE((byte)            0xf2,    2),
    LOAD_INPUT((byte)       0xf3,    2), // 3 Addressing Modes
    OUTPUT((byte)           0xf4,    4), // 3 Addressing Modes
    
    // Comparison
    EQUAL_TO((byte)         0xf5,    4), // 3 Addressing Modes
    NOT_EQUAL_TO((byte)     0xf6,    4), // 3 Addressing Modes
    GREATER_THAN((byte)     0xf7,    4), // 3 Addressing Modes
    
    // Terminate
    TERMINATE((byte)        0xf8,    0);
    
    public byte code;
    public int length;
    Instruction(byte code, int length) {
        this.code = code;
        this.length = length;
    }
    
    public static Instruction fromCode(byte code) {
        for(Instruction instruction: values())
            if(instruction.code == code)
                return instruction;
        return NOOP;
    }
}
