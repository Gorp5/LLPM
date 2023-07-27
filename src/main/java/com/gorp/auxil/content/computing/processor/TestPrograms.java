package com.gorp.auxil.content.computing.processor;

public class TestPrograms {
    
    public static final byte[] TEST = {
            (byte)  0xf3,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0x0,
        
            // Test Input
            (byte)  0xf5,
            (byte)  0x1,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x1,
        
            // Branch If
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x15,
        
            // Jump/give @p auxil:deck{INSTRUCTIONS:[B; -13b, 0b, 1b, -14b, 0b, 0b, -11b, 1b, 0b, 0b, 1b, 9b, 0b, 0b, 0b, 21b, 10b, 0b, 0b, 0b, 0b, -12b, 0b, 1b, 0b, -1b, -8b]}
            (byte)  0xa,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x0,
        
            // Output
            (byte)  0xf4,
            (byte)  0x0,
            (byte)  0x1,
            (byte)  0x0 ,
            (byte)  0xff,
        
            // Terminate
            (byte)  0xf8,
    };
    
    public static final byte[] ADD = {
            (byte)  0xf3,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0xf2,
            (byte)  0x0,
        
            (byte)  0xf3,
            (byte)  0x0,
            (byte)  0x0,
        
            (byte)  0xf2,
            (byte)  0x1,
        
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0x0,
            (byte)  0x1,
            (byte)  0x1,
    };
    
    /*
    Fib Numbers:
       1    0x1
       1    0x1
       2    0x2
       3    0x3
       5    0x5
       8    0x8
       13   0xd  (13)
       21   0x15          8th
       34   0x22
       55   0x37
       89   0x59          11th
       144  0x90
       233  0xe9 (14-9)
       377
       610
       987
       1597
       2584
     */
    
    public static final byte[] FIBBONACI = {
            // Load 1 into memory
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x1,
            // Store memory into register 1
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0x1,
            // Store memory into register 2
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0x2,
        
            
            // Load 0 into memory
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x0,
            // Store memory into register 254
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfe,
            
            
            // Load 1 into memory
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x1,
            // Store memory into register 253
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfd, //17
        
            
            // Load 2 into memory
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x2, //20
            // Store memory into register 252
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfc,
            
            
            // Load input 1 into memory
            (byte)  0xf3,
            (byte)  0x0,
            (byte)  0x1,
            // Store memory into register 255
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xff,
            
            
            // Check if not equal
            (byte)  0xf6,
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x1,
            (byte)  0xff, //32
        
            
            // Conditional Branch
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x3B,
        
            
            // Output (End Program)
            (byte)  0xf4,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x2,
            (byte)  0xff,
            
            // New Instructions
            // Bitshift right 4
            (byte)  0xf,
            (byte)  0x2,
            (byte)  0xff,
            (byte)  0x0,
            (byte)  0x4,
        
            // Output
            (byte)  0xf4,
            (byte)  0x0,
            (byte)  0x1,
            (byte)  0x2,
            (byte)  0xff,
            
            // Terminate
            (byte)  0xf8,
            
            
            // Load contents of previous index
            (byte)  0xf1,
            (byte)  0x2,
            (byte)  0xfe,
            
            
            // Store into next index
            (byte)  0xf2,
            (byte)  0x1,
            (byte)  0xfc,
            
            
            // Add current and next indices
            (byte)  0x1,
            (byte)  0x2,
            (byte)  0xfc,
            (byte)  0x2,
            (byte)  0xfd,
        
            
            // Increment pointers
            (byte)  0x1, //53
            (byte)  0x1,
            (byte)  0xfc,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfe,
            (byte)  0x0,
            (byte)  0x1,
        
            // Loop Back
            (byte)  0xa,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x21,
    };
    
    /*
    Prime Numbers:
       2    2
       3    3
       5    5
       7    7
       11   b
       13   d
       17   11
       19   13
       23   17
       29   1D (1:13)
       31   1F (1:15)       11th
       37   25
       41   29
       43   2B (2:11)
       47   2F (2:15)
     */
    public static final byte[] NTH_PRIME = {
            
            // Set 0 and 1 and 255 to 1
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0x0,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfe,
        
            
            // Load input 1 into memory
            (byte)  0xf3,
            (byte)  0x0,
            (byte)  0x1,
            
            // Store memory into register 255
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xff,
        
            // Store memory into register 255
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfc,
        
            (byte)  0xe,
            (byte)  0x1,
            (byte)  0xff,
            (byte)  0x0,
            (byte)  0x2,
            
            // sieve increment loop
            // Increase 254
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfe,
            (byte)  0x0,
            (byte)  0x1,
            
            // Check if greater than
            (byte)  0xf7, // 19
            (byte)  0x1,
            (byte)  0xfe,
            (byte)  0x1,
            (byte)  0xff,
        
            // Conditional Branch
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x49, //placeholder
            // To Output Finding
            
            // Load 1 into memory
            (byte)  0xf1,
            (byte)  0x1,
            (byte)  0xfe,
            // Store memory into register 253
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfd,
            
            // Sieve set loop
            // Check if greater than
            (byte)  0xf7,
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x1,
            (byte)  0xff,
            
            // Conditional Branch
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x1a, //placeholder
            // To Sieve Increase Loop
            // 19
            
            // Add to get next index
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x1,
            (byte)  0xfe,
        
            // Sieve Value
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x1,
        
            (byte)  0xf2,
            (byte)  0x1,
            (byte)  0xfd,
        
            // Loop Back
            (byte)  0xa,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x2f, //placeholder
            // To Sieve Set Loop
            // 40
        
            //
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x0,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfe,
        
            (byte)  0xf1,
            (byte)  0x0,
            (byte)  0x0,
        
            (byte)  0xf2,
            (byte)  0x0,
            (byte)  0xfd,
        
            
        
            // Check if equal to
        
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfe,
            (byte)  0x0,
            (byte)  0x1, //5b
            
            (byte)  0xf5, // 4d
            (byte)  0x2,
            (byte)  0xfe,
            (byte)  0x0,
            (byte)  0x0,
        
            // Conditional Branch
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x69, //placeholder
        
            // Loop Back
            (byte)  0xa,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x55, //placeholder
        
            (byte)  0x1, //61
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x0,
            (byte)  0x1,
            
            // Check if equal to
            (byte)  0xf5,
            (byte)  0x1,
            (byte)  0xfd,
            (byte)  0x1,
            (byte)  0xfc,
        
            // Conditional Branch
            (byte)  0x9,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x7d, //placeholder
        
            // Loop Back
            (byte)  0xa,
            (byte)  0x0,
            (byte)  0x0, // placeholder
            (byte)  0x0,
            (byte)  0x55, //placeholder
            
        
            // Output (End Program)
            (byte)  0xf4,
            (byte)  0x0,
            (byte)  0x0,
            (byte)  0x1,
            (byte)  0xfe,
        
            // New Instructions
            // Bitshift right 4
            (byte)  0xf,
            (byte)  0x1,
            (byte)  0xfe,
            (byte)  0x0,
            (byte)  0x4,
        
            // Output
            (byte)  0xf4,
            (byte)  0x0,
            (byte)  0x1,
            (byte)  0x1,
            (byte)  0xfe,
            
            // Terminate
            (byte)  0xf8,
            
    };
}
