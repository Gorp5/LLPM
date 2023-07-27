package com.gorp.auxil.content.computing.programmer;

import com.gorp.auxil.AllGuiTextures;
import com.gorp.auxil.AllItems;
import com.gorp.auxil.Lang;
import com.gorp.auxil.content.computing.card.DeckItem;
import com.gorp.auxil.content.computing.processor.Instruction;
import com.gorp.auxil.foundation.gui.ScrollWidget;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/*  FIXME:  Resizing the window while the GUI is active breaks the functionality of the left and right buttons.
 *          They still move the current card left and right, but also change it's position to be in the top left corner
 *          of the screen nad reset it's data.
 */

/*  FIXME:  The Labels of the GUI and Widgets are in the wrong place and display the wrong text.
 */

/*  TODO: Nicer Screen
 */

public class ProgrammerContainerScreen extends ContainerScreen<ProgrammerContainer> {
    
    int currentCard = -1;
    ArrayList<Card> cards = new ArrayList<>();
    
    protected AllGuiTextures background, card;
    private ProgrammerTileEntity te;
    private ScrollWidget presets;
    private int lastModification;
    private int itemLabelOffset;
    private int textureXShift;
    private int itemYShift;
    private ProgrammerContainer container;
    protected List<Widget> widgets = new ArrayList();
    protected int windowXOffset;
    protected int windowYOffset;
    //private String lang = "gui.auxil.programmer.description.";
    private String message_lang = "gui.programmer.full_message";
    
    private int cardPosX, cardPosY;
    
    public ProgrammerContainerScreen(ProgrammerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.te = container.te;
        this.container = container;
        this.background = AllGuiTextures.PROGRAMMER;
        this.card = AllGuiTextures.CARD5;
    }
    
    public void setTitleLabel(int labelHeight, int labelCenter) {
        // Reflection: This probably isn't good, but the inventoryLableY field is protected...
        try {
            Class containerScreen = ContainerScreen.class;
            Field inventoryLabelYReflected  = containerScreen.getDeclaredField("inventoryLabelY");
            Field inventoryLabelXReflected  = containerScreen.getDeclaredField("inventoryLabelX");
            inventoryLabelYReflected.setAccessible(true);
            inventoryLabelYReflected.set(this, labelHeight);
            inventoryLabelXReflected.setAccessible(true);
            inventoryLabelXReflected.set(this, labelCenter);
            
            //this.inventoryLabelY = 85;
        } catch(Exception e) {}
    }
    
    public void setGuiTitleLabel(int labelHeight, int labelCenter) {
        // Reflection: This probably isn't good, but the inventoryLableY field is protected...
        try {
            Class containerScreen = ContainerScreen.class;
            Field titleLabelYReflected  = containerScreen.getDeclaredField("titleLabelY");
            Field titleLabelXReflected  = containerScreen.getDeclaredField("titleLabelX");
            titleLabelYReflected.setAccessible(true);
            titleLabelYReflected.set(this, labelHeight);
            titleLabelXReflected.setAccessible(true);
            titleLabelXReflected.set(this, labelCenter);
            
            //this.inventoryLabelY = 85;
        } catch(Exception e) {}
    }
    
    protected void init() {
        this.setWindowSize(Math.max(this.background.width + 30, AllGuiTextures.PROGRAMMER.width), this.background.height + 4 + AllGuiTextures.PROGRAMMER.height);
        this.setWindowOffset(0, 0);
        this.textureXShift = (this.imageWidth - (this.background.width - 8)) / 2;
        this.itemYShift = -16;
        this.widgets.clear();
        super.init();
        
        cardPosX = 17 + this.leftPos + this.textureXShift;
        cardPosY = 39 + this.topPos;
    
        Button saveButton = new Button(111 + this.leftPos + this.textureXShift, 159 + this.topPos, 16, 16, new StringTextComponent(""), (button) -> this.SaveDeck());
        this.widgets.add(saveButton);
        List<ITextComponent> options = Arrays.stream(Instruction.values())
                .map((instruction) -> new StringTextComponent(instruction.name()))
                .collect(Collectors.toList());
        presets = new InstructionWidget(this.leftPos + this.textureXShift + 71, this.topPos + 130, 16, 17, this).forOptions(options);
        this.widgets.add(presets);
        // Temporary
        Card card = new Card(cardPosX, cardPosY, this);
        this.cards.add(card);
        this.currentCard = 0;
        this.widgets.add(card);
    }
    
    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {}
    
    private void setWindowSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }
    
    private void setWindowOffset(int xOffset, int yOffset) {
        this.windowXOffset = xOffset;
        this.windowYOffset = yOffset;
    }
    
    private void SaveDeck() {
        ItemStack stack = new ItemStack(AllItems.deck.get());
        CompoundNBT tag = stack.getOrCreateTag();
        ArrayList<Byte> data = new ArrayList<>();
        for (Card card : cards)
            data.addAll(Arrays.asList(card.data).stream().map((b) -> (byte) (Integer.reverse(b) >> 24)).collect(Collectors.toList()));
        byte[] bArray = ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
        tag.putByteArray(DeckItem.instrutionKey, bArray);
        stack.setTag(tag);
        //container.getSlot(1).set(stack);
        //container.broadcastChanges();
        PlayerEntity player = container.playerInventory.player;
        int freeSlot = player.inventory.getFreeSlot();
        if(freeSlot == -1)
            player.displayClientMessage(Lang.translate(message_lang), true);
        else {
            player.inventory.add(freeSlot, stack);
        }
    }
    
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        partialTicks = Minecraft.getInstance().getFrameTime();
        this.renderBackground(matrixStack);
        this.renderWindow(matrixStack, mouseX, mouseY, partialTicks);
        Iterator var5 = this.widgets.iterator();
        
        while(var5.hasNext()) {
            Widget widget = (Widget)var5.next();
            widget.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        
        setTitleLabel(285 - 94 + 34 - 16 - 5 - 8, 24);
        setGuiTitleLabel(2, 32);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.disableRescaleNormal();
        RenderHelper.turnOff();
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();
    }
    
    protected void renderWindow(MatrixStack ms, int i, int i1, float v) {
        int invX = this.getLeftOfCentered(AllGuiTextures.PROGRAMMER.width);
        int invY = this.topPos + this.background.height + 4;
        this.renderPlayerInventory(ms, invX, invY);
        int x = this.leftPos + this.textureXShift;
        int y = this.topPos;
        this.background.draw(ms, this, x, y);
    }
    
    public int getLeftOfCentered(int textureWidth) {
        return (this.width - textureWidth) / 2;
    }
    
    // TODO: New player Inventory
    public void renderPlayerInventory(MatrixStack ms, int x, int y) {
        AllGuiTextures.PLAY_INV_PROGRAMMER.draw(ms, this, x - 14, y + 1);
        //this.font.draw(ms, this.inventory.getDisplayName(), (float)(x + 8), (float)(y + 6), 4210752);
    }
    
    @Override
    public boolean mouseClicked(double x, double y, int button) {
        boolean result = super.mouseClicked(x, y, button);
        for(Widget w: this.widgets) {
            boolean flag = w.mouseClicked(x, y, button);
            if(flag)
                result = true;
        }
        int plusX = 110 + this.leftPos + this.textureXShift;
        int plusY = 130 + this.topPos;
        int plusD = 15;
        
        if (x > plusX && x < plusX + plusD && y > plusY && y < plusY + plusD) {
            result = true;
            cards.add(new Card(cardPosX, cardPosY, this));
            this.widgets.remove(2);
            currentCard = cards.size() - 1;
            this.widgets.add(cards.get(currentCard));
        }
        
        int rX = 33 + this.leftPos + this.textureXShift;
        int lX = 16 + this.leftPos + this.textureXShift;
        
        int aY = 127 + this.topPos;
        int aW = 10;
        int aH = 20;
        
        if (x > rX && x < rX + aW && y > aY && y < aY + aH) {
            result = true;
            this.widgets.remove(2);
            currentCard = Math.min(currentCard + 1, cards.size() - 1);
            this.widgets.add(cards.get(currentCard));
        } else if (x > lX && x < lX + aW && y > aY && y < aY + aH) {
            result = true;
            this.widgets.remove(2);
            currentCard = Math.max(currentCard - 1, 0);
            this.widgets.add(cards.get(currentCard));
        }
        
        rX = 126 + this.topPos;
        lX = 139 + this.topPos;
    
        aY = 49 + this.leftPos + this.textureXShift;
        aW = 10;
        aH = 20;
        if (y > rX && y < rX + aW && x > aY && x < aY + aH) {
            int newSize = Math.min(Math.max(this.cards.get(currentCard).getSize() + 1, 3), 5);
            this.cards.get(currentCard).changeSize(newSize);
        } else if (y > lX && y < lX + aW && x > aY && x < aY + aH) {
            int newSize = Math.min(Math.max(this.cards.get(currentCard).getSize() - 1, 3), 5);
            this.cards.get(currentCard).changeSize(newSize);
        }
        
        return result;
    }
    
    public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
        Iterator var4 = this.widgets.iterator();
        
        Widget widget;
        do {
            if (!var4.hasNext()) {
                if (super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_)) {
                    return true;
                }
                
                InputMappings.Input mouseKey = InputMappings.getKey(code, p_keyPressed_2_);
                if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
                    this.onClose();
                    return true;
                }
                
                return false;
            }
            
            widget = (Widget)var4.next();
        } while(!widget.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_));
        
        return true;
    }
    
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Iterator var7 = this.widgets.iterator();
        
        Widget widget;
        do {
            if (!var7.hasNext()) {
                return super.mouseScrolled(mouseX, mouseY, delta);
            }
            
            widget = (Widget)var7.next();
        } while(!widget.mouseScrolled(mouseX, mouseY, delta));
        
        return true;
    }
    
    public boolean mouseReleased(double x, double y, int button) {
        boolean result = false;
        Iterator var7 = this.widgets.iterator();
        
        while(var7.hasNext()) {
            Widget widget = (Widget)var7.next();
            if (widget.mouseReleased(x, y, button)) {
                result = true;
            }
        }
        
        return result | super.mouseReleased(x, y, button);
    }
    
    public void tick() {
        super.tick();
        //this.menu.playerInventory.player.closeContainer();
    }
    
    public void removed() {
    
    }
}
