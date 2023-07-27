package com.gorp.auxil;

import com.gorp.auxil.content.computing.programmer.ProgrammerContainer;
import com.gorp.auxil.content.computing.programmer.ProgrammerContainerScreen;
import com.tterrag.registrate.util.entry.ContainerEntry;
import net.minecraft.inventory.container.Container;

public class AllContainerTypes {
    private static final AuxilRegistrate REGISTRATE = Auxiliaries.registrate();
    public static ContainerEntry<? extends Container> programmer;
    static {
        programmer = REGISTRATE
                .container("programmer", ProgrammerContainer::new, () -> ProgrammerContainerScreen::new)
                .register();
    }
    
    public static void register() {}
}
