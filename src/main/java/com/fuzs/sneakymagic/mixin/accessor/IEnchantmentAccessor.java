package com.fuzs.sneakymagic.mixin.accessor;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface IEnchantmentAccessor {

    @Accessor
    void setType(EnchantmentType type);

}
