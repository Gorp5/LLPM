package com.gorp.auxil.content.computing.programmer;

import com.gorp.auxil.AllContainerTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

public class ProgrammerContainer extends Container {
    
    public ProgrammerTileEntity te;
    public PlayerInventory playerInventory;
    
    private Runnable slotUpdateListener = () -> {
    };
    private final IInventory cardContainer = new Inventory(3) {
        public void setChanged() {
            super.setChanged();
            ProgrammerContainer.this.slotsChanged(this);
            ProgrammerContainer.this.slotUpdateListener.run();
        }
    };
    
    public ProgrammerContainer(ContainerType<?> type, int id, PlayerInventory inv, PacketBuffer extraData) {
        super(type, id);
    
        ClientWorld world = Minecraft.getInstance().level;
        TileEntity tileEntity = world.getBlockEntity(extraData.readBlockPos());
        this.playerInventory = inv;
        if (tileEntity instanceof ProgrammerTileEntity) {
            this.te = (ProgrammerTileEntity)tileEntity;
            
            //this.te.handleUpdateTag(this.te.getBlockState(), extraData.readNbt());
         }
        init();
    }
    
    public ProgrammerContainer(ContainerType<?> type, int id, PlayerInventory inv, ProgrammerTileEntity te) {
        super(type, id);
        this.te = te;
        this.playerInventory = inv;
        //this.inventory
        init();
    }
    
    private void init() {
        int xOffset = 8;
        int yOffset = 207;
    
        // Inventory
        int hotbarSlot;
        for (hotbarSlot = 0; hotbarSlot < 3; ++hotbarSlot) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(this.playerInventory, col + hotbarSlot * 9 + 9, xOffset + col * 18, yOffset + hotbarSlot * 18));
            }
        }
    
        for (hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(this.playerInventory, hotbarSlot, xOffset + hotbarSlot * 18, yOffset + 58));
        }
    
        xOffset = 0;
        yOffset = 0;
    
        // GUI Stuff
//        hotbarSlot = 0;
//        this.addSlot(new Slot(te, hotbarSlot, 86 + xOffset, 160 + yOffset));
//        this.addSlot(new Slot(te, ++hotbarSlot, 16 + xOffset, 160 + yOffset));
    }
    
    public static ProgrammerContainer create(int id, PlayerInventory inv, ProgrammerTileEntity te) {
        return new ProgrammerContainer(AllContainerTypes.programmer.get(), id, inv, te);
    }
    
    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return this.te != null;
    }
}
