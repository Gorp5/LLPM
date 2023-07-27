package com.gorp.auxil.foundation.gui;

import com.gorp.auxil.Lang;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ScrollWidget extends ReactiveWidget {
    
    protected int state;
    private int min = 0;
    private int max = 23;
    private int step = 1;
    protected List<ITextComponent> options;
    private IFormattableTextComponent title = Lang.translate("gui.scrollInput.defaultTitle", new Object[0]);
    private final IFormattableTextComponent scrollToSelect = Lang.translate("gui.scrollInput.scrollToSelect", new Object[0]);
    
    public ScrollWidget(int x, int y, int width, int height, ContainerScreen screen) {
        super(x, y, width, height, screen);
        this.state = 0;
    }
    
    public ScrollWidget forOptions(List<ITextComponent> options) {
        this.max = options.size();
        this.options = options;
        updateTooltip();
        return this;
    }
    
    protected void updateTooltip() {
        this.toolTip.clear();
        this.toolTip.add(this.title.plainCopy().withStyle(TextFormatting.BLUE));
        int min = Math.min(this.max - 16, this.state - 7);
        int max = Math.max(this.min + 16, this.state + 8);
        min = Math.max(min, this.min);
        max = Math.min(max, this.max);
        if (this.min + 1 == min) {
            --min;
        }

        if (min > this.min) {
            this.toolTip.add((new StringTextComponent("> ...")).withStyle(TextFormatting.GRAY));
        }

        if (this.max - 1 == max) {
            ++max;
        }

        for(int i = min; i < max; ++i) {
            if (i == this.state) {
                this.toolTip.add(StringTextComponent.EMPTY.plainCopy().append("-> ").append((ITextComponent)this.options.get(i)).withStyle(TextFormatting.WHITE));
            } else {
                this.toolTip.add(StringTextComponent.EMPTY.plainCopy().append("> ").append((ITextComponent)this.options.get(i)).withStyle(TextFormatting.GRAY));
            }
        }

        if (max < this.max) {
            this.toolTip.add((new StringTextComponent("> ...")).withStyle(TextFormatting.GRAY));
        }

        this.toolTip.add(this.scrollToSelect.plainCopy().withStyle(new TextFormatting[]{TextFormatting.DARK_GRAY, TextFormatting.ITALIC}));
    }
    
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.isHovered) {
            return false;
        } else {
            int priorState = this.state;
            int step = (int)Math.signum(delta) * this.step;
            this.state -= step;
            
            this.clampState();
            
            return priorState != this.state;
        }
    }
    
    protected void clampState() {
        if (this.state >= this.max) {
            this.state = this.max - 1;
        }
        
        if (this.state < this.min) {
            this.state = this.min;
        }
        
        this.updateTooltip();
    }
}
