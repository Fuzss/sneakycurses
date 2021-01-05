package com.fuzs.sneakymagic.common;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.config.EntryCollectionBuilder;
import com.fuzs.sneakymagic.mixin.accessor.IEnchantmentAccessor;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class CompatibilityManager {

    private static int typeCounter;

    public static void load() {

        sync(populate());
    }

    private static Map<ForgeConfigSpec.ConfigValue<List<String>>, Predicate<Item>> populate() {

        final Map<ForgeConfigSpec.ConfigValue<List<String>>, Predicate<Item>> types = Maps.newHashMap();

        types.put(ConfigBuildHandler.SWORD_ENCHANTS, item -> item instanceof SwordItem);
        types.put(ConfigBuildHandler.AXE_ENCHANTS, item -> item instanceof AxeItem);
        types.put(ConfigBuildHandler.TRIDENT_ENCHANTS, item -> item instanceof TridentItem);
        types.put(ConfigBuildHandler.BOW_ENCHANTS, item -> item instanceof BowItem);
        types.put(ConfigBuildHandler.CROSSBOW_ENCHANTS, item -> item instanceof CrossbowItem);

        return types;
    }

    private static void sync(Map<ForgeConfigSpec.ConfigValue<List<String>>, Predicate<Item>> types) {

        final EntryCollectionBuilder<Enchantment> collectionBuilder = new EntryCollectionBuilder<>(ForgeRegistries.ENCHANTMENTS);
        types.forEach((key, value) -> collectionBuilder.buildEntrySet(key.get()).forEach(enchantment -> {

            // absolutely have to save this in order to prevent a recursive loop and a StackOverflowError
            // probably necessary due to the way EnchantmentType#create behaves
            final EnchantmentType originalType = enchantment.type;
            final EnchantmentType combinedType = EnchantmentType.create(SneakyMagic.MODID.toUpperCase(Locale.ROOT) + "_TYPE_" + typeCounter++,
                    item -> originalType != null && originalType.canEnchantItem(item) || value.test(item));
            ((IEnchantmentAccessor) enchantment).setType(combinedType);
        }));
    }

}
