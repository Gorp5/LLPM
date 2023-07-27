package com.gorp.auxil.foundation.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketBase {
    public PacketBase() {
    }
    
    public abstract void write(PacketBuffer var1);
    
    public abstract void handle(Supplier<NetworkEvent.Context> var1);
}
