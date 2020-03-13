package com.fuzs.sneakymagic.config;

import com.fuzs.sneakymagic.SneakyMagic;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue INFINITY_MENDING_FIX;
    public static final ForgeConfigSpec.BooleanValue MULTISHOT_PIERCING_FIX;
    public static final ForgeConfigSpec.BooleanValue PROTECTION_FIX;
    public static final ForgeConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ForgeConfigSpec.BooleanValue SELECTIVE_MENDING;
    public static final ForgeConfigSpec.BooleanValue NO_PROJECTILE_RESISTANCE;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_RESTART;
    public static final ForgeConfigSpec.ConfigValue<List<String>> SWORD_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> AXE_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> TRIDENT_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BOW_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> CROSSBOW_ENCHANTS;
    public static final ForgeConfigSpec.BooleanValue HIDE_CURSES;
    public static final ForgeConfigSpec.BooleanValue SHIFT_SHOWS;
    public static final ForgeConfigSpec.BooleanValue COLOR_NAME;
    public static final ForgeConfigSpec.BooleanValue DISGUISE_TAG;
    public static final ForgeConfigSpec.BooleanValue AFFECT_BOOKS;
    public static final ForgeConfigSpec.BooleanValue HIDE_GLINT;
    public static final ForgeConfigSpec.EnumValue<ItemGlintColor> GLINT_COLOR;
    public static final ForgeConfigSpec.EnumValue<ItemGlintColor> CURSED_GLINT_COLOR;

    static {

        BUILDER.push("general");
        INFINITY_MENDING_FIX = ConfigBuildHandler.BUILDER.comment("Allows infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true);
        MULTISHOT_PIERCING_FIX = ConfigBuildHandler.BUILDER.comment("Allows multishot and piercing to be applied at the same time.").define("Multishot Piercing Fix", true);
        PROTECTION_FIX = ConfigBuildHandler.BUILDER.comment("Allows different types of protection to be applied at the same time, just like in early versions of Minecraft 1.14.").define("Protection Fix", false);
        TRUE_INFINITY = ConfigBuildHandler.BUILDER.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true);
        SELECTIVE_MENDING = ConfigBuildHandler.BUILDER.comment("Mending only selects items for repair that actually need it.").define("Selective Mending", true);
        NO_PROJECTILE_RESISTANCE = ConfigBuildHandler.BUILDER.comment("Disables damage immunity when hit by a projectile. Makes it possible for entities to be hit by multiple projectiles at once.").define("No Projectile Immunity", true);
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
        HIDE_CURSES = ConfigBuildHandler.BUILDER.comment("Hide curse enchantments from the item tooltip.").define("Hide Curses", true);
        SHIFT_SHOWS = ConfigBuildHandler.BUILDER.comment("Temporarily disable effects of this mod while any shift key is pressed.").define("Shift Shows Curses", false);
        COLOR_NAME = ConfigBuildHandler.BUILDER.comment("Cursed items have a red name tag instead of a cyan one.").define("Color Item Name", true);
        DISGUISE_TAG = ConfigBuildHandler.BUILDER.comment("Remove one nbt tag entry in case the item is only enchanted with curses.").define("Disguise NBT Tag", false);
        AFFECT_BOOKS = ConfigBuildHandler.BUILDER.comment("Prevent curses from showing even on enchanted books.").define("Affect Books", false);
        HIDE_GLINT = ConfigBuildHandler.BUILDER.comment("Hide the enchantment glint in case the item is solely enchanted with curses.").define("Hide Glint", true);
        GLINT_COLOR = ConfigBuildHandler.BUILDER.comment("Set the default enchantment glint color. Darker colors are less visible.").defineEnum("Glint Color", ItemGlintColor.DEFAULT);
        CURSED_GLINT_COLOR = ConfigBuildHandler.BUILDER.comment("Set the enchantment glint color for cursed items. Darker colors are less visible.").defineEnum("Curse Glint Color", ItemGlintColor.DEFAULT);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SuppressWarnings("unused")
    public enum ItemGlintColor {

        WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GREY, SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK, DEFAULT;

        private final ResourceLocation location;

        ItemGlintColor() {

            String name = this.name().toLowerCase(Locale.ROOT);
            this.location = name.equals("default") ? ItemRenderer.RES_ITEM_GLINT : new ResourceLocation(SneakyMagic.MODID, "textures/misc/enchanted_item_glint_" + name + ".png");
        }

        public ResourceLocation getLocation() {

            return this.location;
        }

        public boolean isDefault() {

            return this == DEFAULT;
        }

    }

}