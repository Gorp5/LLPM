package com.gorp.auxil.foundation.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class ReactiveWidget extends Widget {
    private float z;
    private boolean wasHovered;
    protected List<ITextComponent> toolTip;
    private BiConsumer<Integer, Integer> onClick;
    private ContainerScreen screen;
    
    public ReactiveWidget(int x, int y, int width, int height, ContainerScreen screen) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.wasHovered = false;
        this.screen = screen;
        this.toolTip = new LinkedList();
        this.onClick = (_$, _$$) -> {
        };
    }
    
    public void render(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.isHovered = this.isMouseOver((double)mouseX, (double)mouseY);
            this.beforeRender(ms, mouseX, mouseY, partialTicks);
            super.render(ms, mouseX, mouseY, partialTicks);
            this.renderButton(ms, mouseX, mouseY, partialTicks);
            this.afterRender(ms, mouseX, mouseY, partialTicks);
            this.wasHovered = this.isHovered();
            if(wasHovered)
                this.renderToolTip(ms, mouseX, mouseY);
        }
    }
    
    @Override
    public void renderToolTip(MatrixStack ms, int mouseX, int mouseY) {
        super.renderToolTip(ms, mouseX, mouseY);
        screen.renderComponentTooltip(ms, this.toolTip, mouseX, mouseY);
    }
    
    public void renderButton(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    }
    
    private void beforeRender(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        ms.pushPose();
    }
    
    private void afterRender(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        ms.popPose();
    }
    
    public void runCallback(double mouseX, double mouseY) {
        this.onClick.accept((int)mouseX, (int)mouseY);
    }
    
    public void onClick(double mouseX, double mouseY) {
        this.runCallback(mouseX, mouseY);
    }
    
    public boolean clicked(double mouseX, double mouseY) {
        return this.active && this.visible && this.isMouseOver(mouseX, mouseY);
    }
}
