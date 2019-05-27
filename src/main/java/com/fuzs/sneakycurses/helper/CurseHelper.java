package com.fuzs.sneakycurses.helper;

import com.fuzs.sneakycurses.handler.ConfigHandler;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.List;
import java.util.Map;

public class CurseHelper {

    public static boolean onlyCurses(ItemStack stack) {

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);

        return !enchants.isEmpty() && enchants.keySet().stream().allMatch(Enchantment::isCurse);
    }

    public static boolean isCursed(ItemStack stack) {

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);

        return enchants.keySet().stream().anyMatch(Enchantment::isCurse);
    }

    public static boolean isItemBlacklisted(ItemStack stack) {

        ResourceLocation resource = Item.REGISTRY.getNameForObject(stack.getItem());
        List<String> blacklist = Lists.newArrayList(ConfigHandler.a3hideGlintBlacklist);

        return resource != null && (blacklist.contains(resource.toString()) || blacklist.contains(resource.getResourceDomain()));

    }

    public static boolean hasQuarkRune(ItemStack stack) {
        boolean flag = !stack.isEmpty() && stack.getTagCompound() != null && stack.getTagCompound().getBoolean("Quark:RuneAttached");
        return flag && ConfigHandler.a51QuarkCompat && Loader.isModLoaded("quark");
    }

}
