package com.gorp.auxil.content.computing.programmer;

import com.gorp.auxil.AllTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ProgrammerBlock extends Block {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.programmer");
    public ProgrammerBlock(Properties properties) {
        super(properties);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof ProgrammerTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (ProgrammerTileEntity)te, pos);
            }
            return ActionResultType.SUCCESS;
        }
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return AllTileEntities.PROGRAMMER.get().create();
    }
}
