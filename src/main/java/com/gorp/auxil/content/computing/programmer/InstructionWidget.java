package com.gorp.auxil.content.computing.programmer;

import com.gorp.auxil.content.computing.processor.Instruction;
import com.gorp.auxil.foundation.gui.ScrollWidget;
import net.minecraft.util.text.ITextComponent;

public class InstructionWidget extends ScrollWidget {
    private ProgrammerContainerScreen screen;
    public InstructionWidget(int xIn, int yIn, int widthIn, int heightIn, ProgrammerContainerScreen screen) {
        super(xIn, yIn, widthIn, heightIn, screen);
        this.screen = screen;
    }
    
    public void onClick(double x, double y) {
        super.onClick(x, y);
        ITextComponent text = this.options.get(this.state);
        Instruction instruction = Instruction.valueOf(text.getString());
        Byte[] b = new Byte[instruction.length + 1];
        for(int i=0; i < b.length; i++) {
            if(i==0)
                b[i] = (byte)(Integer.reverse(instruction.code) >>> (Integer.SIZE - Byte.SIZE));
            else
                b[i] = (byte) 0;
        }
        this.screen.cards.get(this.screen.currentCard).setData(b);
    }
    
    public void addTips() {
        // Descriptions and color coding
    }
}
