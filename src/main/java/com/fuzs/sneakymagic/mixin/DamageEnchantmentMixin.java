package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.ExclusivenessElement;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin extends Enchantment {

    protected DamageEnchantmentMixin(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Inject(method = "canApplyTogether", at = @At("HEAD"), cancellable = true)
    public void canApplyTogether(Enchantment ench, CallbackInfoReturnable<Boolean> callbackInfo) {

        ExclusivenessElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_EXCLUSIVENESS);
        if (element.isEnabled() && element.damageFix) {

            if (ench instanceof DamageEnchantment) {

                callbackInfo.setReturnValue(super.canApplyTogether(ench));
            }
        } else if (ench instanceof ImpalingEnchantment) {

            callbackInfo.setReturnValue(false);
        }
    }

}
