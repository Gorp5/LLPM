package com.gorp.auxil;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum AllGuiTextures {
    PROGRAMMER("programmer_gui.png", 0, 0, 144, 185),
    PLAY_INV_PROGRAMMER("inventory.png", 0, 0, 175, 100),
    CARD5("programmer_gui.png", 146, 0, 107, 85),
    CARD4("programmer_gui.png", 146, 86, 107, 70),
    CARD3("programmer_gui.png", 146, 156, 107, 54),
    COVER("programmer_gui.png", 157, 210, 8, 8),
    BLUE_COVER("programmer_gui.png", 95, 187, 8, 8),
    GREEN_COVER("programmer_gui.png", 95, 199, 8, 8),
    YELLOW_COVER("programmer_gui.png", 95, 211, 8, 8),
    RED_COVER("programmer_gui.png", 95, 223, 8, 8),
    BLUE_BASE("programmer_gui.png", 0, 185, 95, 12),
    GREEN__BASE("programmer_gui.png", 0, 197, 95, 12),
    YELLOW_BASE("programmer_gui.png", 0, 209, 95, 12),
    RED__BASE("programmer_gui.png", 0, 221, 95, 12),
    DRAG_BYTE("programmer_gui.png", 0, 0, 95, 12);
    
    public static final int FONT_COLOR = 5726074;
    public final ResourceLocation location;
    public int width;
    public int height;
    public int startX;
    public int startY;
    
    private AllGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }
    
    private AllGuiTextures(int startX, int startY) {
        this("icons.png", startX * 16, startY * 16, 16, 16);
    }
    
    private AllGuiTextures(String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation("auxil", "textures/gui/" + location);
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void bind() {
        Minecraft.getInstance().getTextureManager().bind(this.location);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack ms, AbstractGui screen, int x, int y) {
        this.bind();
        screen.blit(ms, x, y, this.startX, this.startY, this.width, this.height);
    }
    
    public static void register() {}
}
