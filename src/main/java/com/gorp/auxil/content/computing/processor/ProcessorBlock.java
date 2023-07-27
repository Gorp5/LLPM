package com.gorp.auxil.content.computing.processor;

import com.gorp.auxil.AllTileEntities;
import com.gorp.auxil.content.computing.card.DeckItem;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ProcessorBlock extends HorizontalBlock {
    public static final EnumProperty<ProcessorPart> PART = EnumProperty.create("part", ProcessorPart.class);;
    public static final IntegerProperty POWER = RedstoneWireBlock.POWER;
    
    public ProcessorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, ProcessorPart.HEAD).setValue(POWER, 0));
    }
    
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, POWER);
    }
    
    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack stack) {
        super.setPlacedBy(world, pos, state, player, stack);
        if (!world.isClientSide) {
            BlockPos blockpos = pos.relative(state.getValue(FACING));
            world.setBlock(blockpos, state.setValue(PART, ProcessorPart.TAIL).setValue(FACING, state.getValue(FACING).getOpposite()), 3);
            world.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }
    
    @Override
    public int getSignal(BlockState state, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return state.getValue(POWER);
    }
    
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        ItemStack item = player.getItemInHand(hand);
        if(state.getValue(ProcessorBlock.PART) == ProcessorPart.TAIL)
            pos = pos.relative(state.getValue(ProcessorBlock.FACING).getOpposite());
        if(item.isEmpty() || item.getItem() instanceof DeckItem) {
            ProcessorTileEntity tile = (ProcessorTileEntity)world.getBlockEntity(pos);
            ItemStack previous = tile.addDeck(item);
            player.setItemInHand(hand, previous);
            
            return ActionResultType.CONSUME;
        }
        
        return super.use(state, world, pos, player, hand, raytrace);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(ProcessorBlock.PART) == ProcessorPart.HEAD;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return AllTileEntities.CPU.create();
    }
}

enum ProcessorPart implements IStringSerializable {
    HEAD("head"),
    TAIL("tail");
    
    private final String name;
    
    private ProcessorPart(String name_stirng) {
        this.name = name_stirng;
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
}