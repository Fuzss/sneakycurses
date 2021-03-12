package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.element.ExclusivenessElement;
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

        ExclusivenessElement element = (ExclusivenessElement) SneakyMagic.ENCHANTMENT_EXCLUSIVENESS;
        if (element.isEnabled() && element.damageFix) {

            return super.canApplyTogether(ench);
        }

        return !(ench instanceof DamageEnchantment) && super.canApplyTogether(ench);
    }

}
