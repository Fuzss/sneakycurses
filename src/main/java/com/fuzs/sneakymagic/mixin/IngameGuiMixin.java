package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.util.CurseMatcher;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@SuppressWarnings("unused")
@Mixin(IngameGui.class)
public abstract class IngameGuiMixin extends AbstractGui {

    @Shadow
    protected ItemStack highlightingItemStack;

    // renderSelectedItemName
    @Redirect(method = "func_238453_b_", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/text/IFormattableTextComponent;mergeStyle(Lnet/minecraft/util/text/TextFormatting;)Lnet/minecraft/util/text/IFormattableTextComponent;", ordinal = 0))
    public IFormattableTextComponent getCurseColor(IFormattableTextComponent component, TextFormatting format) {

        if (this.highlightingItemStack.isEnchanted() || ConfigBuildHandler.AFFECT_BOOKS.get() && this.highlightingItemStack.getItem() == Items.ENCHANTED_BOOK) {

            // check if item is cursed
            Collection<Enchantment> enchantments = EnchantmentHelper.getEnchantments(this.highlightingItemStack).keySet();
            if (CurseMatcher.anyMatch(enchantments)) {

                if (ConfigBuildHandler.DISGUISE_ITEM.get() && this.highlightingItemStack.getItem() == Items.ENCHANTED_BOOK && CurseMatcher.allMatch(enchantments) || ConfigBuildHandler.COLOR_NAME.get()) {

                    if (ConfigBuildHandler.COLOR_NAME.get()) {

                        return component.mergeStyle(TextFormatting.RED);
                    } else {

                        return component;
                    }
                }
            }
        }

        return component.mergeStyle(format);
    }

}
