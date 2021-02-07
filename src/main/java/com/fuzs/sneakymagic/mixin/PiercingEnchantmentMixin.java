package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.ExclusivenessElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.MultishotEnchantment;
import net.minecraft.enchantment.PiercingEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(PiercingEnchantment.class)
public abstract class PiercingEnchantmentMixin extends Enchantment {

    protected PiercingEnchantmentMixin(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Inject(method = "canApplyTogether", at = @At("HEAD"), cancellable = true)
    public void canApplyTogether(Enchantment ench, CallbackInfoReturnable<Boolean> callbackInfo) {

        ExclusivenessElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_EXCLUSIVENESS);
        if (element.isEnabled() && element.multishotPiercingFix && ench instanceof MultishotEnchantment) {

            callbackInfo.setReturnValue(super.canApplyTogether(ench));
        }
    }

}
