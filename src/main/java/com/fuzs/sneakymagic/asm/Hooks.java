package com.fuzs.sneakymagic.asm;

import com.fuzs.sneakymagic.common.CompatibilityHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class Hooks {

    public static boolean hasInfinity(boolean flag, ItemStack stack) {

        return flag || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
    }

    public static void applyCrossbowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        CompatibilityHandler.addArrowEnchantments(abstractarrowentity, stack);
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0) {

            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }
    }

    public static void applyBowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, stack);
        if (i > 0) {

            abstractarrowentity.setPierceLevel((byte) i);
        }
    }

}
