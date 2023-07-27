package com.gorp.auxil;

import com.gorp.auxil.content.computing.processor.ProcessorTileEntity;
import com.gorp.auxil.content.computing.programmer.ProgrammerTileEntity;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.tileentity.TileEntity;

public class AllTileEntities {
    private static final AuxilRegistrate REGISTRATE = Auxiliaries.registrate();
    public static TileEntityEntry<? extends TileEntity> CPU, PROGRAMMER;
    
    static {
        //================================================================================
        // Blocks
        //================================================================================
        CPU =
             REGISTRATE
                .tileEntity("processor", ProcessorTileEntity::new)
                .validBlocks(AllBlocks.processor)
                .register();
    
        PROGRAMMER =
            REGISTRATE
                .tileEntity("programmer", ProgrammerTileEntity::new)
                .validBlocks(AllBlocks.programmer)
                .register();
    }
    
    public static void register() {}
}
