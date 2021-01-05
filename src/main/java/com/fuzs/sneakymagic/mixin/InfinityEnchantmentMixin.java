package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(InfinityEnchantment.class)
public abstract class InfinityEnchantmentMixin extends Enchantment {

    protected InfinityEnchantmentMixin(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Inject(method = "canApplyTogether", at = @At("HEAD"), cancellable = true)
    public void canApplyTogether(Enchantment ench, CallbackInfoReturnable<Boolean> callbackInfo) {

        if (ConfigBuildHandler.infinityMendingFix && ench instanceof MendingEnchantment) {

            callbackInfo.setReturnValue(super.canApplyTogether(ench));
        }
    }

}
