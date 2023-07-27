package com.gorp.auxil.content.computing.processor;

import com.gorp.auxil.foundation.capability.IProgramCapability;

import java.util.Arrays;

public class InstructionInterpreter {
    
    private static int unsignedByte(byte data) {
        return data & 0xff;
    }
    
    public static void interpret(IProgramCapability program) {
        byte pointer = program.getStackPointer();
        byte[] counter = program.getProgramCounter();
        byte code = program.pullFromRom((byte)(0x1), counter[0], counter[1])[0];
        try {
            switch (code) {
                case 0x0:
                    noop(program);
                    break;
                case 0x1:
                    add(program, program.pullFromRom((byte) ((Instruction.ADD.length + 1)), counter[0], counter[1]));
                    break;
                case 0x2:
                    subtract(program, program.pullFromRom((byte) ((Instruction.SUBTRACT.length + 1)), counter[0], counter[1]));
                    break;
                case 0x3:
                    multiply(program, program.pullFromRom((byte) ((Instruction.MULTIPLY.length + 1)), counter[0], counter[1]));
                    break;
                case 0x4:
                    divide(program, program.pullFromRom((byte) ((Instruction.DIVIDE.length + 1)), counter[0], counter[1]));
                    break;
                case 0x5:
                    and(program, program.pullFromRom((byte) ((Instruction.AND.length + 1)), counter[0], counter[1]));
                    break;
                case 0x6:
                    or(program, program.pullFromRom((byte) ((Instruction.OR.length + 1)), counter[0], counter[1]));
                    break;
                case 0x7:
                    xor(program, program.pullFromRom((byte) ((Instruction.XOR.length + 1)), counter[0], counter[1]));
                    break;
                case 0x8:
                    bitTest(program, program.pullFromRom((byte) ((Instruction.BIT_TEST.length + 1)), counter[0], counter[1]));
                    break;
                case 0x9:
                    branchIf(program, program.pullFromRom((byte) ((Instruction.BRANCH_IF.length + 1)), counter[0], counter[1]));
                    break;
                case 0xa:
                    jump(program, program.pullFromRom((byte) ((Instruction.JUMP.length + 1)), counter[0], counter[1]));
                    break;
                case 0xb:
                    callSubroutine(program, program.pullFromRom((byte) ((Instruction.CALL_SUBROUTINE.length + 1)), counter[0], counter[1]));
                    break;
                case 0xc:
                    returnSubroutine(program);
                    break;
                case 0xd:
                    bitSet(program, program.pullFromRom((byte) ((Instruction.BIT_SET.length + 1)), counter[0], counter[1]));
                    break;
                case 0xe:
                    bitShiftLeft(program, program.pullFromRom((byte) ((Instruction.BIT_SHIFT_LEFT.length + 1)), counter[0], counter[1]));
                    break;
                case 0xf:
                    bitShiftRight(program, program.pullFromRom((byte) ((Instruction.BIT_SHIFT_RIGHT.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf1:
                    load(program, program.pullFromRom((byte) ((Instruction.LOAD.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf2:
                    store(program, program.pullFromRom((byte) ((Instruction.STORE.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf3:
                    loadInput(program, program.pullFromRom((byte) ((Instruction.LOAD_INPUT.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf4:
                    output(program, program.pullFromRom((byte) ((Instruction.OUTPUT.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf5:
                    equalTo(program, program.pullFromRom((byte) ((Instruction.EQUAL_TO.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf6:
                    notEqualTo(program, program.pullFromRom((byte) ((Instruction.EQUAL_TO.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf7:
                    greaterThan(program, program.pullFromRom((byte) ((Instruction.EQUAL_TO.length + 1)), counter[0], counter[1]));
                    break;
                case (byte) 0xf8:
                    terminate(program);
                    break;
            }
        } catch (Exception e) {}
    }
    
    private static void nextInstruction(IProgramCapability program, Instruction instruction) {
        program.addToProgramCounter((byte)0x0000, (byte)(instruction.length + 1));
    }
    
    private static byte addressingHandler(IProgramCapability program, byte mode, byte... operand) {
        switch (mode) {
            case 0x0:
                return operand[0];
            case 0x1:
                return program.accessRegister(operand[0]);
            case 0x2:
                return program.accessRegister(program.accessRegister(operand[0]));
            default:
                return 0x0;
        }
    }
    
    public static void terminate(IProgramCapability program) {
        program.stop();
    }
    
    public static void noop(IProgramCapability program) {
        nextInstruction(program, Instruction.NOOP);
    }
    
    // Arithmetic
    public static void add(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        int value = operand1 + operand2;
        if(value > 0xff) {
            // Carry bit, or throw error
        }
        
        if((instruction[1] & 0x3) != 0)
            program.storeInRegister((byte)value, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)value, instruction[1]);
        }
        
        nextInstruction(program, Instruction.ADD);
    }
    
    public static void subtract(IProgramCapability program, byte[] instruction) {
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        int value = operand1 - operand2;
        if(value > 0xff) {
            // Carry bit, or throw error
        }
    
        if((instruction[1] & 0x1) != 0)
            program.storeInRegister((byte)value, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)value, instruction[1]);
        }
        nextInstruction(program, Instruction.SUBTRACT);
    }
    
    public static void multiply(IProgramCapability program, byte[] instruction) {
        if(instruction[1] == 0xff) {
            // Throw IndexOutOfBounds Exception
            return;
        }
        
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        int value = operand1 * operand2;
        
        program.storeInRegister((byte)(value >> 8), instruction[1]);
        program.storeInRegister((byte)(value & 0xff), (byte)(instruction[1] + 1));
        nextInstruction(program, Instruction.MULTIPLY);
    }
    
    public static void divide(IProgramCapability program, byte[] instruction) {
        if(instruction[1] == 0xff) {
            // Throw IndexOutOfBounds Exception
            return;
        }
        
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        int remainder = operand1 % operand2;
        int dividend = Math.floorDiv(operand1,  operand2);
        
        program.storeInRegister((byte)(dividend), instruction[1]);
        program.storeInRegister((byte)(remainder), (byte)(instruction[1] + 1));
        nextInstruction(program, Instruction.DIVIDE);
    }
    
    // Comparison program.setComparisonTestBit(and);
    public static void and(IProgramCapability program, byte[] instruction) {
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        byte and = (byte)(operand1 & operand2);
        if((instruction[1] & 0x1) != 0)
            program.storeInRegister((byte)and, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)and, instruction[1]);
        }
        nextInstruction(program, Instruction.AND);
    }
    
    public static void or(IProgramCapability program, byte[] instruction) {
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        byte or = (byte)(operand1 | operand2);
        if((instruction[1] & 0x1) != 0)
            program.storeInRegister((byte)or, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)or, instruction[1]);
        }
        nextInstruction(program, Instruction.OR);
    }
    
    public static void xor(IProgramCapability program, byte[] instruction) {
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        byte xor = (byte)(operand1 ^ operand2);
        if((instruction[1] & 0x1) != 0)
            program.storeInRegister((byte)xor, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)xor, instruction[1]);
        }
        nextInstruction(program, Instruction.XOR);
    }
    
    public static void bitTest(IProgramCapability program, byte[] instruction) {
        byte operand1 = program.accessRegister(instruction[1]);
        byte operand2 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        byte bitTest = (byte)((operand1 >> operand2) & 0x1);
        program.setComparisonTestBit(bitTest);
        nextInstruction(program, Instruction.BIT_TEST);
    }
    
    public static void equalTo(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        boolean equal = operand1 == operand2;
        program.setComparisonTestBit(equal);
        nextInstruction(program, Instruction.EQUAL_TO);
    }
    
    public static void notEqualTo(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        boolean equal = operand1 != operand2;
        program.setComparisonTestBit(equal);
        nextInstruction(program, Instruction.NOT_EQUAL_TO);
    }
    
    public static void greaterThan(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        boolean great = unsignedByte(operand1) > unsignedByte(operand2);
        program.setComparisonTestBit(great);
        nextInstruction(program, Instruction.GREATER_THAN);
    }
    
    // Control Flow
    public static void branchIf(IProgramCapability program, byte[] instruction) {
        if(((program.getStatus() >> 1) & 0x1) == 1) {
            byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
            byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
            program.setProgramCounter(operand1, operand2);
            program.setComparisonTestBit(false);
            return;
        }
    
        program.setComparisonTestBit(false);
        nextInstruction(program, Instruction.BRANCH_IF);
    }
    
    public static void jump(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        program.setProgramCounter(operand1, operand2);
    }
    
    public static void callSubroutine(IProgramCapability program, byte[] instruction) {
        program.pushOnStack(program.getProgramCounter()[0]);
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        program.setProgramCounter(operand1, operand2);
    }
    
    public static void returnSubroutine(IProgramCapability program) {
        program.setProgramCounter((byte)0x0, program.pullOffStack());
        nextInstruction(program, Instruction.CALL_SUBROUTINE);
    }
    
    // Bitwise
    public static void bitSet(IProgramCapability program, byte[] instruction) {
        byte originalByte = program.accessRegister(instruction[1]);
        byte operand1 = addressingHandler(program, instruction[2], Arrays.copyOfRange(instruction, 3, instruction.length));
        byte operand2 = addressingHandler(program, instruction[4], Arrays.copyOfRange(instruction, 5, instruction.length));
        
        program.storeInRegister(instruction[1], (byte)(originalByte | (byte)((operand2 & operand1) << 1)));
        nextInstruction(program, Instruction.BIT_SET);
    }
    
    public static void bitShiftLeft(IProgramCapability program, byte[] instruction) {
        byte originalByte = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand1 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        byte value = (byte)(originalByte << operand1);
        
        if((instruction[1] & 0x3) != 0)
            program.storeInRegister((byte)value, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)value, instruction[1]);
        }
        
        nextInstruction(program, Instruction.BIT_SHIFT_LEFT);
    }
    
    public static void bitShiftRight(IProgramCapability program, byte[] instruction) {
        byte originalByte = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand1 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
    
        byte value = (byte)(originalByte >> operand1);
    
        if((instruction[1] & 0x3) != 0)
            program.storeInRegister((byte)value, addressingHandler(program, (byte)(instruction[1] - 1), Arrays.copyOfRange(instruction, 2, instruction.length)));
        else {
            program.storeInRegister((byte)value, instruction[1]);
        }
        
        nextInstruction(program, Instruction.BIT_SHIFT_RIGHT);
    }
    
    public static void load(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        program.setMemory(operand1);
        nextInstruction(program, Instruction.LOAD);
    }
    
    public static void store(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        program.storeInRegister(program.getMemory(), operand1);
        nextInstruction(program, Instruction.STORE);
    }
    
    public static void loadInput(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        program.setMemory(program.inputFrom(operand1));
        nextInstruction(program, Instruction.LOAD_INPUT);
    }
    
    public static void output(IProgramCapability program, byte[] instruction) {
        byte operand1 = addressingHandler(program, instruction[1], Arrays.copyOfRange(instruction, 2, instruction.length));
        byte operand2 = addressingHandler(program, instruction[3], Arrays.copyOfRange(instruction, 4, instruction.length));
        program.outputTo(operand1, (byte)(operand2 & 0xf));
        nextInstruction(program, Instruction.OUTPUT);
    }
}
