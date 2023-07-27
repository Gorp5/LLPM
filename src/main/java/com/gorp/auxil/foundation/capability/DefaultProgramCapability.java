package com.gorp.auxil.foundation.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class DefaultProgramCapability implements ICapabilityProvider, IProgramCapability {
    
    private short PROGRAM_COUNTER;
    private byte STACK_POINTER;
    private byte STATUS;
    private byte MEMORY;
    private boolean STOPPED = false;
    private BitSet ROM = new BitSet(4096);
    private NonNullList<Byte> REGISTERS = NonNullList.withSize(256, (byte)0x0);
    private NonNullList<Byte> STACK = NonNullList.withSize(256, (byte)0x0);
    private NonNullList<Byte> INPUT = NonNullList.withSize(2, (byte)0x0);
    private NonNullList<Byte> OUTPUT = NonNullList.withSize(2, (byte)0x0);
    
    private int unsignedByte(byte data) {
        return data & 0xff;
    }
    
    private int unsignedShort(short data) {
        return data & 0xffff;
    }
    
    @Override
    public byte[] getProgramCounter() {
        return new byte[]{(byte)(PROGRAM_COUNTER >> 8), (byte)(PROGRAM_COUNTER & 0xff)};
    }
    
    @Override
    public void setProgramCounter(byte upperByte, byte lowerByte) {
        PROGRAM_COUNTER = (short)(upperByte << 8 | lowerByte);
    }
    
    @Override
    public void addToProgramCounter(byte upperByte, byte lowerByte) {
        int newCounter = PROGRAM_COUNTER + (upperByte << 8) + unsignedByte(lowerByte);
        if(newCounter > 0xff) {
            // Throw IndexOutOfBounds Exception
            return;
        }
        
        PROGRAM_COUNTER = (short)(newCounter);
    }
    
    @Override
    public void subtractFromProgramCounter(byte upperByte, byte lowerByte) {
        int newCounter = PROGRAM_COUNTER - (upperByte << 8) - unsignedByte(lowerByte);
        if(newCounter < 0x0) {
            // Throw IndexOutOfBounds Exception
            return;
        }
    
        PROGRAM_COUNTER = (short)(newCounter);
    }
    
    @Override
    public BitSet getROM() {
        return ROM;
    }
    
    @Override
    public void setROM(BitSet newRom) {
        ROM = newRom;
    }
    
    @Override
    public byte[] pullFromRom(byte length, byte upperByte, byte lowerByte) {
        int index = (unsignedByte(upperByte) << 8) + unsignedByte(lowerByte);
        byte[] extra = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        return ArrayUtils.addAll(ROM.get(index * 8, index * 8 + unsignedByte(length) * 8).toByteArray(), extra);
    }
    
    @Override
    public byte getStackPointer() {
        return STACK_POINTER;
    }
    
    @Override
    public void setStackPointer(byte pointer) {
        STACK_POINTER = pointer;
    }
    
    @Override
    public void addToStackPointer(byte addend) {
        int newPointer = STACK_POINTER + unsignedByte(addend);
        if(newPointer > 0xff) {
            // Throw IndexOutOfBounds Exception
            return;
        }
    
        STACK_POINTER = (byte)newPointer;
    }
    
    @Override
    public void subtractFromStackPointer(byte minuend) {
        int newPointer = STACK_POINTER - unsignedByte(minuend);
        if(newPointer < 0x0) {
            // Throw IndexOutOfBounds Exception
            return;
        }
    
        STACK_POINTER = (byte)newPointer;
    }
    
    @Override
    public void pushOnStack(byte data) {
        if(STACK_POINTER == (byte)0xff) {
            // Throw StackOverflow Error
            return;
        } else
            STACK_POINTER++;
        STACK.set(unsignedByte(STACK_POINTER), data);
    }
    
    @Override
    public byte pullOffStack() {
        if(STACK_POINTER == (byte)0x0) {
            // Throw StackOverflow Error
            return 0x0;
        } else {
            STACK_POINTER--;
            return STACK.get(STACK_POINTER);
        }
    }
    
    @Override
    public byte accessRegister(byte address) {
        return REGISTERS.get(unsignedByte(address));
    }
    
    @Override
    public void storeInRegister(byte data, byte address) {
        REGISTERS.set(unsignedByte(address), data);
    }
    
    @Override
    public void outputTo(byte pin, byte data) {
        OUTPUT.set(unsignedByte(pin), data);
    }
    
    @Override
    public byte outputOf(byte pin) {
        return OUTPUT.get(unsignedByte(pin));
    }
    
    @Override
    public byte inputFrom(byte pin) {
        return INPUT.get(unsignedByte(pin));
    }
    
    public void inputTo(byte pin, byte input) {
        INPUT.set(unsignedByte(pin), input);
    }
    
    @Override
    public byte getStatus() {
        return STATUS;
    }
    
    @Override
    public void setComparisonTestBit(byte test) {
        byte setTest = (byte)(test & 0x1);
        if(setTest == ((STATUS >> 1) & 0x1))
            return;
    
        STATUS ^= (byte)(1 << 1);
    }
    
    @Override
    public void setComparisonTestBit(boolean test) {
        byte setTest = (byte)(test ? 0x1: 0x0);
        if(setTest == ((STATUS >> 1) & 0x1))
            return;
        
        STATUS ^= (byte)(1 << 1);
    }
    
    @Override
    public byte getMemory() {
        return MEMORY;
    }
    
    @Override
    public void setMemory(byte memory) {
        MEMORY = memory;
    }
    
    public void stop() {
        STOPPED = true;
    }
    
    public boolean isStopped() {
        return  STOPPED;
    }
    


//    @Override
//    public byte[] getProgramCounter() {
//        return new byte[]{(byte)(PROGRAM_COUNTER >> 8 & 0xff), (byte)(PROGRAM_COUNTER & 0xff)};
//    }
//
//    @Override
//    public void setProgramCounter(byte... count) {
//        PROGRAM_COUNTER = (short) (count[0]<<8 | count[1] & 0xFF);
//    }
//
//    @Override
//    public void displaceProgramCounter(byte... displacement) {
//        PROGRAM_COUNTER += (short) (displacement[0]<<8 | displacement[1] & 0xFF);
//    }
//
//    @Override
//    public BitSet getROM() {
//        return ROM;
//    }
//
//    @Override
//    public void setROM(BitSet newRom) {
//        ROM.clear();
//        ROM.or(newRom);
//    }
//
//    @Override
//    public BitSet pullFromRom(byte length, byte... address) {
//        int index = unsignedShort((short) (address[0]<<8 | address[1] & 0xFF));
//        return ROM.get(index, index + unsignedByte(length));
//    }
//
//    @Override
//    public byte getStackPointer() {
//        return STACK_POINTER;
//    }
//
//    @Override
//    public void setStackPointer(byte pointer) {
//        STACK_POINTER = pointer;
//    }
//
//    @Override
//    public void pushOnStack(byte data) {
//        if(STACK_POINTER == (byte)0xff) {
//            // Throw StackOverflow Error
//            return;
//        } else
//            STACK_POINTER++;
//        STACK.set(unsignedByte(STACK_POINTER), data);
//    }
//
//    @Override
//    public byte pullOffStack() {
//        if(STACK_POINTER == 0x0) {
//            // Throw IndexOutOfBoundsException
//            return (byte)0x0;
//        }
//
//        STACK_POINTER--;
//        byte data = STACK.get(unsignedByte(STACK_POINTER));
//        STACK.set(unsignedByte(STACK_POINTER), (byte)0x0);
//        return data;
//    }
//
//    @Override
//    public byte accessRegister(byte address) {
//        return REGISTERS.get(unsignedByte(address));
//    }
//
//    @Override
//    public void storeInRegister(byte data, byte address) {
//        REGISTERS.set(unsignedByte(address), data);
//    }
//
//    @Override
//    public void putOnDatabus(byte data, byte pin) {
//        if((pin & (byte)0x1) == (byte)0x0) {
//            // Throw CantOutputOnBus Error
//            return;
//        }
//
//        DATABUS.set(unsignedByte(pin), data);
//    }
//
//    @Override
//    public byte pullOffDatabus(byte pin) {
//        if((pin & (byte)0x1) == (byte)0x1) {
//            // Throw CantInputOnBus Error
//            return 0;
//        }
//
//        return DATABUS.get(unsignedByte(pin));
//    }
//
//    @Override
//    public void stop() {
//        // Stop
//    }
//
//    @Override
//    public void start() {
//        // Start
//    }
    
    @Override
    public void writeToNBT(CompoundNBT tag) {
        tag.putShort("PROGRAM_COUNTER", PROGRAM_COUNTER);
        tag.putByte("STACK_POINTER", STACK_POINTER);
        
        tag.putByteArray("ROM", ROM.toByteArray());
        tag.putByteArray("REGISTERS", ArrayUtils.toPrimitive(REGISTERS.toArray(new Byte[0])));
        tag.putByteArray("STACK", ArrayUtils.toPrimitive(STACK.toArray(new Byte[0])));
        tag.putByteArray("INPUT", ArrayUtils.toPrimitive(INPUT.toArray(new Byte[0])));
        tag.putByteArray("OUTPUT", ArrayUtils.toPrimitive(OUTPUT.toArray(new Byte[0])));
    }
    
    @Override
    public void readFromNBT(CompoundNBT tag) {
        PROGRAM_COUNTER = tag.getShort("PROGRAM_COUNTER");
        STACK_POINTER = tag.getByte("STACK_POINTER");
        ROM = BitSet.valueOf(tag.getByteArray("ROM"));
        REGISTERS = NonNullList.create();
        REGISTERS.addAll(new ArrayList(Collections.singletonList(tag.getByteArray("REGISTERS"))));
        STACK = NonNullList.create();
        STACK.addAll(new ArrayList(Collections.singletonList(tag.getByteArray("STACK"))));
        INPUT = NonNullList.create();
        INPUT.addAll(new ArrayList(Collections.singletonList(tag.getByteArray("INPUT"))));
        OUTPUT = NonNullList.create();
        OUTPUT.addAll(new ArrayList(Collections.singletonList(tag.getByteArray("OUTPUT"))));
    }
    
    @Override
    public void onContentsChanged() {
    
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.PROGRAM_CAPABILITY)
            return LazyOptional.of(() -> (this)).cast();
        return null;
    }
}
