package com.gorp.auxil;

import com.gorp.auxil.content.computing.processor.ProcessorBlock;
import com.gorp.auxil.content.computing.programmer.ProgrammerBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;

public class AllBlocks {
    private static final AuxilRegistrate REGISTRATE = Auxiliaries.registrate();
    public static BlockEntry<? extends Block> processor, programmer;
    
    static {
        processor =
                REGISTRATE
                .block("processor", ProcessorBlock::new)
                .initialProperties(() -> Blocks.GLASS)
                .defaultLoot()
                .addLayer(() -> RenderType::cutoutMipped)
                .item()
                .group(() -> Auxiliaries.creativeTab)
                .lang("Processor")
                .build()
                .register();
        
        programmer =
                REGISTRATE
                .block("programmer", ProgrammerBlock::new)
                .initialProperties(() -> Blocks.GLASS)
                .defaultLoot()
                .addLayer(() -> RenderType::cutoutMipped)
                .item()
                .group(() -> Auxiliaries.creativeTab)
                .lang("Programmer")
                .build()
                .register();
    }
    
    public static void register() {}
}
