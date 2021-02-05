package com.fuzs.sneakymagic.mixin.client;

import com.fuzs.sneakymagic.client.element.SneakyCursesElement;
import com.fuzs.sneakymagic.common.SneakyMagicElements;
import com.fuzs.sneakymagic.common.util.CurseMatcher;
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

            SneakyCursesElement element = SneakyMagicElements.getAs(SneakyMagicElements.SNEAKY_CURSES);
            callbackInfo.setReturnValue(!element.isEnabled() || !element.disguiseItem ||
                    !CurseMatcher.allMatch(EnchantmentHelper.getEnchantments(stack).keySet()));
        }
    }

}
