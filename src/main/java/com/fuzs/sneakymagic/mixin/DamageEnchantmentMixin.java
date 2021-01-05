package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.*;
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

        if (ConfigBuildHandler.DAMAGE_FIX.get()) {

            if (ench instanceof DamageEnchantment) {

                callbackInfo.setReturnValue(super.canApplyTogether(ench));
            }
        } else if (ench instanceof ImpalingEnchantment) {

            callbackInfo.setReturnValue(false);
        }
    }

}
