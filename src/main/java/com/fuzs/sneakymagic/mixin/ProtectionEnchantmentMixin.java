package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.ExclusivenessElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin extends Enchantment {

    protected ProtectionEnchantmentMixin(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Inject(method = "canApplyTogether", at = @At("HEAD"), cancellable = true)
    public void canApplyTogether(Enchantment ench, CallbackInfoReturnable<Boolean> callbackInfo) {

        ExclusivenessElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_EXCLUSIVENESS);
        if (element.isEnabled() && element.protectionFix && ench instanceof ProtectionEnchantment) {

            callbackInfo.setReturnValue(super.canApplyTogether(ench));
        }
    }

}
