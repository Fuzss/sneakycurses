package fuzs.sneakycurses.util;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Objects;

public class CurseMatcher {
    public static boolean anyEnchantIsCursed(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).keySet().stream()
                .filter(Objects::nonNull)
                .anyMatch(Enchantment::isCurse);
    }

    public static boolean allEnchantsAreCursed(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).keySet().stream()
                .filter(Objects::nonNull)
                .allMatch(Enchantment::isCurse);
    }

    public static boolean isAffected(ItemStack stack) {
        if (stack.isEmpty() || SneakyCurses.CONFIG.get(ClientConfig.class).shiftShows && Screen.hasShiftDown()) return false;
        if (stack.getItem() instanceof EnchantedBookItem) {
            if (!SneakyCurses.CONFIG.get(ClientConfig.class).affectBooks) return false;
            return anyEnchantIsCursed(stack) && !allEnchantsAreCursed(stack);
        }
        return anyEnchantIsCursed(stack);
    }
}
