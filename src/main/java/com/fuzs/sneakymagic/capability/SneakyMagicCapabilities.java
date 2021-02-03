package com.fuzs.sneakymagic.capability;

import com.fuzs.puzzleslib_sm.capability.CapabilityController;
import com.fuzs.sneakymagic.capability.container.ArrowCapability;
import com.fuzs.sneakymagic.capability.container.IArrowCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SneakyMagicCapabilities extends CapabilityController {

    @CapabilityInject(IArrowCapability.class)
    public static final Capability<ArrowCapability> ARROW_ENCHANTMENTS = null;

    @Override
    protected void register() {

        register(IArrowCapability.class, ArrowCapability::new);
    }

}
