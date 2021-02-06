package com.fuzs.sneakymagic.client.element;

import com.fuzs.puzzleslib_sm.element.extension.ElementExtension;
import com.fuzs.puzzleslib_sm.element.side.IClientElement;
import com.fuzs.sneakymagic.common.element.EasyEnchantingElement;
import net.minecraftforge.common.ForgeConfigSpec;

public class EasyEnchantingExtension extends ElementExtension<EasyEnchantingElement> implements IClientElement {

    public boolean allEnchantments;

    public EasyEnchantingExtension(EasyEnchantingElement parent) {

        super(parent);
    }

    @Override
    public void setupClientConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("When hovering over an enchanting option show the complete outcome on the tooltip.").define("Show All Enchantments", false), v -> this.allEnchantments = v);
    }

}
