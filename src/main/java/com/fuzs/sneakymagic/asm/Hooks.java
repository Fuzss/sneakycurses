package com.fuzs.sneakymagic.asm;

import com.fuzs.sneakymagic.client.CursedTooltipHandler;
import com.fuzs.sneakymagic.common.CompatibilityHandler;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;

import java.util.Objects;

@SuppressWarnings({"unused", "JavadocReference"})
public class Hooks {

    /**
     * makes it possible to apply infinity and mending together in {@link net.minecraft.enchantment.InfinityEnchantment#canApplyTogether}
     */
    public static boolean canApplyMending(boolean flag) {

        return !ConfigBuildHandler.INFINITY_MENDING_FIX.get() && flag;
    }

    /**
     * makes it possible to apply multishot and piercing together in {@link net.minecraft.enchantment.PiercingEnchantment#canApplyTogether}
     */
    public static boolean canApplyPiercing(boolean flag, Enchantment ench) {

        return flag && (ConfigBuildHandler.MULTISHOT_PIERCING_FIX.get() || ench != Enchantments.MULTISHOT);
    }

    /**
     * makes it possible to apply multishot and piercing together in {@link net.minecraft.enchantment.MultishotEnchantment#canApplyTogether}
     */
    public static boolean canApplyMultishot(boolean flag, Enchantment ench) {

        return flag && (ConfigBuildHandler.MULTISHOT_PIERCING_FIX.get() || ench != Enchantments.PIERCING);
    }

    /**
     * makes different types of protection compatible with each other in {@link net.minecraft.enchantment.ProtectionEnchantment#canApplyTogether}
     */
    public static boolean canApplyProtection() {

        return !ConfigBuildHandler.PROTECTION_FIX.get();
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

    /**
     * checks a few more curse related conditions to see if the item effect should be rendered in {@link net.minecraft.item.Item#hasEffect}
     */
    public static boolean hasEffect(boolean isEnchanted, ItemStack stack) {

        return isEnchanted && !(CursedTooltipHandler.getEnchantments(stack.getEnchantmentTagList()).keySet().stream().filter(Objects::nonNull)
                .allMatch(Enchantment::isCurse) && ConfigBuildHandler.HIDE_GLINT.get());
    }

}
