package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mixin(ImpalingEnchantment.class)
public abstract class ImpalingEnchantmentMixin extends Enchantment {

    protected ImpalingEnchantmentMixin(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Override
    public boolean canApplyTogether(@Nonnull Enchantment ench) {

        if (!ConfigBuildHandler.DAMAGE_FIX.get()) {

            return !(ench instanceof DamageEnchantment) && super.canApplyTogether(ench);
        }

        return super.canApplyTogether(ench);
    }

}
