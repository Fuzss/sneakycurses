package com.fuzs.sneakymagic.element;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import net.minecraftforge.common.ForgeConfigSpec;

public class ExclusivenessElement extends AbstractElement implements ICommonElement {

    public boolean infinityMendingFix;
    public boolean multishotPiercingFix;
    public boolean protectionFix;
    public boolean damageFix;
    
    @Override
    public String getDescription() {

        return "Make many enchantments no longer mutually exclusive.";
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Allow infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true), v -> this.infinityMendingFix = v);
        addToConfig(builder.comment("Allow multishot and piercing to be applied at the same time.").define("Multishot Piercing Fix", true), v -> this.multishotPiercingFix = v);
        addToConfig(builder.comment("Allow different types of protection to be applied at the same time.").define("Protection Fix", false), v -> this.protectionFix = v);
        addToConfig(builder.comment("Allow different types of damage enchantments to be applied at the same time.").define("Damage Fix", false), v -> this.damageFix = v);
    }

}
