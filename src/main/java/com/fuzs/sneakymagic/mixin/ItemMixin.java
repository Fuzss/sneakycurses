package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.util.CurseMatcher;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(Item.class)
public abstract class ItemMixin extends ForgeRegistryEntry<Item> {

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    public void hasEffect(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {

        if (stack.isEnchanted()) {

            callbackInfo.setReturnValue(!ConfigBuildHandler.DISGUISE_ITEM.get() ||
                    !CurseMatcher.allMatch(EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTagList()).keySet()));
        }
    }

}
