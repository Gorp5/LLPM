package com.gorp.auxil.content.computing.programmer;

import com.gorp.auxil.AllGuiTextures;
import com.gorp.auxil.content.computing.processor.Instruction;
import com.gorp.auxil.foundation.gui.ReactiveWidget;
import com.mojang.blaze3d.matrix.MatrixStack;
import javafx.util.Pair;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Card extends ReactiveWidget {
    final static int width = AllGuiTextures.CARD5.width;
    final static int height = AllGuiTextures.CARD5.height;
    private static Pair<Integer, Integer> ButtonOffsets = new Pair<>(12, 16);
    private static int buttonX = 8;
    private int buttonY = 5, yOffset = 0;
    public Byte[] data = new Byte[5];
    private Screen parentScreen;
    private static AllGuiTextures[] CARD_ELEMENTS = {AllGuiTextures.CARD3, AllGuiTextures.CARD4, AllGuiTextures.CARD5};
    private String[] COLOR_CODING = new String[]{"NONE", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"}; //AllGuiTextures.OP_CODE, AllGuiTextures.ADDRESSING_MODE, AllGuiTextures.DATA};
    private static Map<String, Pair< AllGuiTextures,  AllGuiTextures>> COLORS = new HashMap<String, Pair< AllGuiTextures,  AllGuiTextures>>() {{
        put("BLUE", new Pair<>(AllGuiTextures.BLUE_BASE, AllGuiTextures.BLUE_COVER));
        put("YELLOW",  new Pair<>(AllGuiTextures.YELLOW_BASE, AllGuiTextures.YELLOW_COVER));
        put("RED",  new Pair<>(AllGuiTextures.RED__BASE, AllGuiTextures.RED_COVER));
        put("GREEN",  new Pair<>(AllGuiTextures.GREEN__BASE, AllGuiTextures.GREEN_COVER));
    }};
    
    public Card(int x, int y, ContainerScreen parent) {
        super(x, y, width, height, parent);
        this.buttonY = 5;
        data = new Byte[this.buttonY];
        for (int i = 0; i < this.buttonY; i++) {
            data[i] = (byte) 0;
        }
        parentScreen = parent;
    }
    
    public Card(int x, int y, ContainerScreen parent, Byte[] bytes) {
        super(x, y, width, height, parent);
        parentScreen = parent;
        data = bytes;
    }
    
    public int getSize() {
        return this.buttonY;
    }
    
    public void changeSize(int newSize) {
        int prevSize = this.buttonY;
        this.buttonY = newSize;
        Byte[] newData = new Byte[newSize];
        System.arraycopy(this.data, 0, newData, 0, Math.min(newSize, this.data.length));
        for(int i = this.data.length; i < newSize; i++)
            newData[i] = (byte) 0;
        this.data = newData;
    }
    
    public void setData(Byte[] data) {
        this.data = data;
        changedInstructionCode(data[0]);
    }
    
    public void changedInstructionCode(Byte code) {
        Instruction i = colorCoding(code);
        changeSize(i.length + 1);
    }
    
    @Override
    public void onClick(double mouseX, double mouseY) {
        int index = indexHovered(mouseX, mouseY);
        if(index > -1 && index / 8 < data.length) {
            data[index / 8] = (byte) (data[index / 8] ^ (1 << (index % 8)));
            if (index < 8)
                changedInstructionCode(data[0]);
        }
    }
    
    public int indexHovered(double mouseX, double mouseY) {
        double adjustedX = mouseX - x - 5;
        double adjustedY = mouseY - y - 6 - this.yOffset;
        
        double xIndex = (adjustedX / 12) % 8;
        double yIndex = (adjustedY / 16);
        
        if (xIndex > 0 && yIndex > 0 && (yIndex % 1) < .75) {
            return (int) (xIndex) + (int) (yIndex) * 8;
        }
        
        return -1;
    }
    
    public Instruction colorCoding(Byte data) {
        Instruction i = Instruction.fromCode((byte) (Integer.reverse(data) >>> (Integer.SIZE - Byte.SIZE)));
        this.COLOR_CODING[0] = "BLUE";
        int offset = 1;
        int temp = 0;
        for (int x = 0; x <= i.length; x += 2) {
            this.COLOR_CODING[x + offset] = "GREEN";
            this.COLOR_CODING[x + offset + 1] = "YELLOW";
            temp = x;
        }
        for (int x = temp; x < i.length; x++) {
            this.COLOR_CODING[x + offset] = "NONE";
        }
        return i;
    }
    
    public void renderButton(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        switch(this.buttonY) {
            case 5:
                AllGuiTextures.CARD5.draw(ms, parentScreen,this.x, this.y);
                yOffset = 0;
                break;
            case 4:
                yOffset = (AllGuiTextures.CARD5.height - AllGuiTextures.CARD4.height) / 2;
                AllGuiTextures.CARD4.draw(ms, parentScreen,this.x, this.y + yOffset);
                break;
            default:
                yOffset = (AllGuiTextures.CARD5.height - AllGuiTextures.CARD3.height) / 2;
                AllGuiTextures.CARD3.draw(ms, parentScreen,this.x, this.y + yOffset);
                break;
        }
    
        int startX = 5 + this.x;
        int startY = 6 + this.y + yOffset;
        
        int color_offset = 0;
        for(int f = 0; f < this.data.length; f++) {
            String col = this.COLOR_CODING[f];
            if(col != "NONE") {
                Pair<AllGuiTextures, AllGuiTextures> texs = this.COLORS.get(col);
                AllGuiTextures tex = texs.getKey(); // Base
                tex.draw(ms, parentScreen, startX, startY + color_offset);
            }
            color_offset += 16;
        }
        
        startX += 5;
        startY += 2;
        
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < this.data.length; y++) {
                try {
                    if ((this.data[y] >> x & 1) == 0) {
                        String col = this.COLOR_CODING[y];
                        AllGuiTextures tex;
                        if(col != "NONE") {
                            Pair<AllGuiTextures, AllGuiTextures> texs = this.COLORS.get(col);
                            tex = texs.getValue(); // Cover
                        } else {
                            tex = AllGuiTextures.COVER;
                        }
                        
                        tex.draw(ms, parentScreen, startX + x * 11, startY + y * 16);
                    }
                } catch(Exception ignored) {
                    int debug = 0;
                }
            }
        }
    }
}
