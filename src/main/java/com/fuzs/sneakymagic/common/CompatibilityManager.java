package com.fuzs.sneakymagic.common;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.config.StringListBuilder;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class CompatibilityManager {

    private final StringListBuilder<Enchantment> parser = new StringListBuilder<>(ForgeRegistries.ENCHANTMENTS);
    // save this so it also requires a restart itself
    private final boolean requireRestart = ConfigBuildHandler.REQUIRE_RESTART.get();

    private final Map<ForgeConfigSpec.ConfigValue<List<String>>, Predicate<Item>> types = Maps.newHashMap();
    private final Map<Enchantment, EnchantmentType> defaultTypes = Maps.newHashMap();
    private int typeCounter;

    public CompatibilityManager() {

        this.populate();
        this.sync();
    }

    public void onModConfig(final ModConfig.ModConfigEvent evt) {

        if (evt.getConfig().getSpec() == ConfigBuildHandler.SPEC) {

            if (!this.requireRestart) {

                this.sync();
            }
        }
    }

    private void populate() {

        this.types.put(ConfigBuildHandler.SWORD_ENCHANTS, item -> item instanceof SwordItem);
        this.types.put(ConfigBuildHandler.AXE_ENCHANTS, item -> item instanceof AxeItem);
        this.types.put(ConfigBuildHandler.TRIDENT_ENCHANTS, item -> item instanceof TridentItem);
        this.types.put(ConfigBuildHandler.BOW_ENCHANTS, item -> item instanceof BowItem);
        this.types.put(ConfigBuildHandler.CROSSBOW_ENCHANTS, item -> item instanceof CrossbowItem);
    }

    private void sync() {

        // reset all types to default state
        if (!this.requireRestart) {

            this.defaultTypes.forEach((key, value) -> key.type = value);
        }

        this.types.forEach((key, value) -> this.parser.buildEntrySet(key.get()).forEach(ench -> {

            // absolutely have to save this in order to prevent a recursive loop and a StackOverflowError
            // probably necessary due to the way EnchantmentType#create behaves
            final EnchantmentType type = ench.type;
            // save default types for resetting later
            if (!this.requireRestart) {

                this.defaultTypes.putIfAbsent(ench, type);
            }
            ench.type = EnchantmentType.create(SneakyMagic.MODID.toUpperCase(Locale.ROOT) + "_TYPE_" + this.typeCounter++,
                    item -> type != null && type.canEnchantItem(item) || value.test(item));
        }));
    }

}
