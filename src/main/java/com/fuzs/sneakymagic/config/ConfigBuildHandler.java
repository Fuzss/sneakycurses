package com.fuzs.sneakymagic.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue INFINITY_MENDING_FIX;
    public static final ForgeConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ForgeConfigSpec.BooleanValue SELECTIVE_MENDING;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_RESTART;
    public static final ForgeConfigSpec.ConfigValue<List<String>> SWORD_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> AXE_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> TRIDENT_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BOW_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> CROSSBOW_ENCHANTS;

    static {

        BUILDER.push("general");
        INFINITY_MENDING_FIX = ConfigBuildHandler.BUILDER.comment("Allows infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true);
        TRUE_INFINITY = ConfigBuildHandler.BUILDER.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true);
        SELECTIVE_MENDING = ConfigBuildHandler.BUILDER.comment("Mending only selects items for repair that actually need it.").define("Selective Mending", true);
        BUILDER.pop();

        BUILDER.push("compatibility");
        REQUIRE_RESTART = ConfigBuildHandler.BUILDER.comment("Changes made to this section of the config file require a restart to apply. Disabling this setting may have unforeseen technical consequences.").define("Require Restart", true);
        SWORD_ENCHANTS = ConfigBuildHandler.BUILDER.comment("Additional enchantments to be made applicable to swords. Format for every entry is \"<namespace>:<id>,<value>\".").define("Sword Enchantments", Lists.newArrayList("minecraft:impaling"));
        AXE_ENCHANTS = ConfigBuildHandler.BUILDER.comment("Additional enchantments to be made applicable to axes. Format for every entry is \"<namespace>:<id>,<value>\".").define("Axe Enchantments", Lists.newArrayList("minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods", "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting", "minecraft:sweeping", "minecraft:impaling"));
        TRIDENT_ENCHANTS = ConfigBuildHandler.BUILDER.comment("Additional enchantments to be made applicable to tridents. Format for every entry is \"<namespace>:<id>,<value>\".").define("Trident Enchantments", Lists.newArrayList("minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods", "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting", "minecraft:sweeping", "minecraft:quick_charge"));
        BOW_ENCHANTS = ConfigBuildHandler.BUILDER.comment("Additional enchantments to be made applicable to bows. Format for every entry is \"<namespace>:<id>,<value>\".").define("Bow Enchantments", Lists.newArrayList("minecraft:piercing", "minecraft:multishot", "minecraft:quick_charge"));
        CROSSBOW_ENCHANTS = ConfigBuildHandler.BUILDER.comment("Additional enchantments to be made applicable to crossbows. Format for every entry is \"<namespace>:<id>,<value>\".").define("Crossbow Enchantments", Lists.newArrayList("minecraft:flame", "minecraft:punch", "minecraft:power", "minecraft:infinity"));
        BUILDER.pop();

        BUILDER.push("curses");
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}