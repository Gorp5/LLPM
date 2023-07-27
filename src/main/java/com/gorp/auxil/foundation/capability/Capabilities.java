package com.gorp.auxil.foundation.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IProgramCapability.class)
    public static final Capability<IProgramCapability> PROGRAM_CAPABILITY = null;
}
