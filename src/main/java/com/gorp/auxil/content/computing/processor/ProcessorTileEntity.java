package com.gorp.auxil.content.computing.processor;

import com.gorp.auxil.content.computing.card.DeckItem;
import com.gorp.auxil.foundation.capability.DefaultProgramCapability;
import com.gorp.auxil.foundation.capability.IProgramCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.BitSet;

public class ProcessorTileEntity extends TileEntity implements ITickableTileEntity {
    
    public IProgramCapability cpuCap;
    public LazyOptional<IProgramCapability> lazyCPU =  LazyOptional.of(() -> this.cpuCap);
    
    public IItemHandler deckCap;
    public LazyOptional<IItemHandler> lazyDeck =  LazyOptional.of(() -> this.deckCap);
    
    boolean isActive = false;
    public byte[] program = new byte[0]; //TestPrograms.TEST;

    public byte[] getProgram() {
        return program;
    }
    
    public ProcessorTileEntity(TileEntityType<?> type) {
        super(type);
        resetCAP(this.program);
        deckCap = new ItemStackHandler();
    }
    
    private void resetCAP(byte[] ar) {
        BitSet set = BitSet.valueOf(ar);
        cpuCap = new DefaultProgramCapability();
        cpuCap.setROM(set);
    }
    
    public ItemStack addDeck(ItemStack nextDeck) {
        ItemStack curr = deckCap.extractItem(0, 1, false);
        deckCap.insertItem(0, nextDeck, false);
        CompoundNBT tag = nextDeck.getTag();
        if(tag != null && tag.contains(DeckItem.instrutionKey)) {
            byte[] bytes = tag.getByteArray(DeckItem.instrutionKey);
            BitSet set = BitSet.valueOf(bytes);
            this.program = bytes;
            cpuCap.setROM(set);
        }
        return curr;
    }
    //            /give @p auxil:deck{INSTUCTIONS:[B;-15b, 0b, 1b, -14b, 0b, 1b, -14b, 0b, 2b, -15b, 0b, 0b, -14b, 0b, -2b, -15b, 0b, 1b, -14b, 0b, -3b, -15b, 0b, 2b, -14b, 0b, -4b, -13b, 0b, 1b, -14b, 0b, -1b, -10b, 1b, -3b, 1b, -1b, 9b, 0b, 0b, 0b, 59b, -12b, 0b, 0b, 2b, -1b, 15b, 2b, -1b, 0b, 4b, -12b, 0b, 1b, 2b, -1b, -8b, -15b, 2b, -2b, -14b, 1b, -4b, 1b, 2b, -4b, 2b, -3b, 1b, 1b, -4b, 0b, 1b, 1b, 1b, -3b, 0b, 1b, 1b, 1b, -2b, 0b, 1b, 10b, 0b, 0b, 0b, 33b]}
    //            /give @p auxil:deck{INSTUCTIONS:[B;
    //            -13b, 0b, 1b,
    //            -14b, 0b, 0b,
    //            -13b, 0b, 0b,
    //            -14b, 0b, 1b,
    
    //            ]}
    //            (byte)  0xf3,
    //            (byte)  0x0,
    //            (byte)  0x1,
    //
    //            (byte)  0xf2,
    //            (byte)  0x0,
    //
    //            (byte)  0xf3,
    //            (byte)  0x0,
    //            (byte)  0x0,
    //
    //            (byte)  0xf2,
    //            (byte)  0x1,
    //
    //            (byte)  0x1,
    //            (byte)  0x1,
    //            (byte)  0x0,
    //            (byte)  0x1,
    //            (byte)  0x1,
    //
    //            (byte)  0xf4,
    //            (byte)  0x0,
    //            (byte)  0x0,
    //            (byte)  0x1,
    //            (byte)  0x0,]
    
    @Override
    public void tick() {
        BlockPos otherPos = this.worldPosition.relative(getBlockState().getValue(ProcessorBlock.FACING).getOpposite());
        if(level.getDirectSignal(otherPos, getBlockState().getValue(ProcessorBlock.FACING).getOpposite()) > 0)
            isActive = true;
        else {
            isActive = false;
            this.resetCAP(this.program);
        }

        if(isActive && !cpuCap.isStopped() && !level.isClientSide) {
            int cycles = 2;
            for(int x = 0; x < cycles; x++) {
                Direction input = getBlockState().getValue(ProcessorBlock.FACING).getClockWise();
                Direction output = getBlockState().getValue(ProcessorBlock.FACING).getCounterClockWise();
    
                // Read input
                if (level.getSignal(this.worldPosition.relative(input), input) > 0) {
                    cpuCap.inputTo((byte) 0x0, (byte) level.getSignal(this.worldPosition.relative(input), input));
                }
    
                if (level.getSignal(this.worldPosition.relative(getBlockState().getValue(ProcessorBlock.FACING)).relative(input), input) > 0) {
                    cpuCap.inputTo((byte) 0x1, (byte) level.getSignal(this.worldPosition.relative(getBlockState().getValue(ProcessorBlock.FACING)).relative(input), input));
                }
    
                InstructionInterpreter.interpret(cpuCap);
    
                // Output
                level.setBlock(getBlockPos(), getBlockState().setValue(ProcessorBlock.POWER, unsignedByte(cpuCap.outputOf((byte) 0x0))), 3);
                level.setBlock(getBlockPos().relative(getBlockState().getValue(ProcessorBlock.FACING)), getBlockState().setValue(ProcessorBlock.POWER, unsignedByte(cpuCap.outputOf((byte) 0x1))).setValue(ProcessorBlock.PART, ProcessorPart.TAIL).setValue(ProcessorBlock.FACING, getBlockState().getValue(ProcessorBlock.FACING).getOpposite()), 3);
            }
        }
    }
    
    private int unsignedByte(byte data) {
        return data & 0xff;
    }
}
