package com.fuzs.sneakymagic.asm;

import com.fuzs.sneakymagic.common.CompatibilityHandler;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;

@SuppressWarnings({"unused", "JavadocReference"})
public class Hooks {

    /**
     * makes it possible to apply infinity and mending together in {@link net.minecraft.enchantment.InfinityEnchantment#canApplyTogether}
     */
    public static boolean canApplyMending(boolean flag) {

        return !ConfigBuildHandler.INFINITY_MENDING_FIX.get() && flag;
    }

    /**
     * generates ammo out of thin air in {@link net.minecraft.item.CrossbowItem#hasAmmo}
     */
    public static boolean hasInfinity(boolean flag, ItemStack stack) {

        return flag || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
    }

    /**
     * add bow enchantments to crossbow arrow and properly set infinity pickup status in {@link net.minecraft.item.CrossbowItem#createArrow}
     */
    public static void applyCrossbowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        CompatibilityHandler.applyCrossbowEnchantments(abstractarrowentity, stack);
    }

}
