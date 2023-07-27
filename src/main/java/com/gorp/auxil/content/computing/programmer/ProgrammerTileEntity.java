package com.gorp.auxil.content.computing.programmer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class ProgrammerTileEntity extends LockableLootTileEntity implements INamedContainerProvider {
    static final int slots = 2;
    NonNullList<ItemStack> cards = NonNullList.withSize(slots, ItemStack.EMPTY);
    
    public ProgrammerTileEntity(TileEntityType<?> type) {
        super(type);
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return cards;
    }
    
    @Override
    protected void setItems(NonNullList<ItemStack> itemStacks) {
        cards = itemStacks;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Card Programmer");
    }
    
    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("Card Programmer");
    }
    
    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory) {
        return ProgrammerContainer.create(id, inventory, this);
    }
    
    @Override
    public int getContainerSize() {
        return slots;
    }
    
    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        if (!this.trySaveLootTable(tag)) {
            ItemStackHelper.saveAllItems(tag, this.cards);
        }
    
        return tag;
    }
    
    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.cards = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ItemStackHelper.loadAllItems(tag, this.cards);
        }
    }
}
