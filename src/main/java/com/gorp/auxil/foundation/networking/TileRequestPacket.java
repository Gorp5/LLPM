package com.gorp.auxil.foundation.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TileRequestPacket extends PacketBase {
    //private DebugOverlayRenderer renderer;
    private BlockPos position;
    
    public TileRequestPacket(BlockPos pos) {
        this.position = pos;
    }
    
    public TileRequestPacket(PacketBuffer buffer) {
        this.position = buffer.readBlockPos();
    }
    
    public void write(PacketBuffer buffer) {
        buffer.writeBlockPos(position);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.level;
                if (world != null && !world.isEmptyBlock(this.position)) {
                    TileEntity tileEntity = world.getBlockEntity(this.position);
                    //DebugOverlayRenderer.setTile(tileEntity);
                    // DebugOverlayRenderer No Longer Exists
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
