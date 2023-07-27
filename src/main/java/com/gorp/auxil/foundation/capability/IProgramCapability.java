package com.gorp.auxil.foundation.capability;

import net.minecraft.nbt.CompoundNBT;

import java.util.BitSet;

public interface IProgramCapability {
    
    // Program Counter
    byte[] getProgramCounter();
    void setProgramCounter(byte upperByte, byte lowerByte);
    void addToProgramCounter(byte upperByte, byte lowerByte);
    void subtractFromProgramCounter(byte upperByte, byte lowerByte);
    
    // ROM
    BitSet getROM();
    void setROM(BitSet newRom);
    byte[] pullFromRom(byte length, byte upperByte, byte lowerByte);
    
    // Stack Pointer
    byte getStackPointer();
    void setStackPointer(byte pointer);
    void addToStackPointer(byte addend);
    void subtractFromStackPointer(byte minuend);
    
    // Stack Interaction
    void pushOnStack(byte data);
    byte pullOffStack();
    
    // Register Interaction
    byte accessRegister(byte address);
    void storeInRegister(byte data, byte address);
    
    // Databus Interaction
    void outputTo(byte pin, byte data);
    byte outputOf(byte pin);
    byte inputFrom(byte pin);
    void inputTo(byte pin, byte input);
    
    // Status
    byte getStatus();
    void setComparisonTestBit(byte test);
    void setComparisonTestBit(boolean test);
    void stop();
    boolean isStopped();
    
    // Memory
    byte getMemory();
    void setMemory(byte memory);
    
    void writeToNBT(CompoundNBT tag);
    void readFromNBT(CompoundNBT tag);
    void onContentsChanged();
}
