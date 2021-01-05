package com.fuzs.sneakymagic.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // general
    public static final ForgeConfigSpec.BooleanValue INFINITY_MENDING_FIX;
    public static final ForgeConfigSpec.BooleanValue MULTISHOT_PIERCING_FIX;
    public static final ForgeConfigSpec.BooleanValue PROTECTION_FIX;
    public static final ForgeConfigSpec.BooleanValue DAMAGE_FIX;
    public static final ForgeConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ForgeConfigSpec.BooleanValue NO_PROJECTILE_RESISTANCE;
    // compatibility
    public static final ForgeConfigSpec.ConfigValue<List<String>> SWORD_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> AXE_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> TRIDENT_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BOW_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> CROSSBOW_ENCHANTS;
    // curses
    public static final ForgeConfigSpec.BooleanValue HIDE_CURSES;
    public static final ForgeConfigSpec.BooleanValue DISGUISE_ITEM;
    public static final ForgeConfigSpec.BooleanValue SHIFT_SHOWS;
    public static final ForgeConfigSpec.BooleanValue COLOR_NAME;
    public static final ForgeConfigSpec.BooleanValue DISGUISE_TAG;
    public static final ForgeConfigSpec.BooleanValue AFFECT_BOOKS;

    static {

        BUILDER.push("general");
        INFINITY_MENDING_FIX = ConfigBuildHandler.BUILDER.comment("Allows infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true);
        MULTISHOT_PIERCING_FIX = ConfigBuildHandler.BUILDER.comment("Allows multishot and piercing to be applied at the same time.").define("Multishot Piercing Fix", true);
        PROTECTION_FIX = ConfigBuildHandler.BUILDER.comment("Allows different types of protection to be applied at the same time, just like in early versions of Minecraft 1.14.").define("Protection Fix", false);
        DAMAGE_FIX = ConfigBuildHandler.BUILDER.comment("Allows different types of damage enchantments to be applied at the same time.").define("Damage Fix", false);
        TRUE_INFINITY = ConfigBuildHandler.BUILDER.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true);
        NO_PROJECTILE_RESISTANCE = ConfigBuildHandler.BUILDER.comment("Disables damage immunity when hit by a projectile. Makes it possible for entities to be hit by multiple projectiles at once.").define("No Projectile Immunity", true);
        BUILDER.pop();

        BUILDER.comment("Changes made to this section of the config file require a restart to apply.", "Format for every entry is \"<namespace>:<path>\". Path may use asterisk as wildcard parameter.");
        BUILDER.push("compatibility");
        String compatibility = "Additional enchantments to be made applicable to ";
        SWORD_ENCHANTS = ConfigBuildHandler.BUILDER.comment(compatibility + "swords.").define("Sword Enchantments", getEnchantmentList(Enchantments.IMPALING));
        AXE_ENCHANTS = ConfigBuildHandler.BUILDER.comment(compatibility + "axes.").define("Axe Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.IMPALING));
        TRIDENT_ENCHANTS = ConfigBuildHandler.BUILDER.comment(compatibility + "tridents.").define("Trident Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.QUICK_CHARGE));
        BOW_ENCHANTS = ConfigBuildHandler.BUILDER.comment(compatibility + "bows.").define("Bow Enchantments", getEnchantmentList(Enchantments.PIERCING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE));
        CROSSBOW_ENCHANTS = ConfigBuildHandler.BUILDER.comment(compatibility + "crossbows.").define("Crossbow Enchantments", getEnchantmentList(Enchantments.FLAME, Enchantments.PUNCH, Enchantments.POWER, Enchantments.INFINITY));
        BUILDER.pop();

        BUILDER.push("curses");
        HIDE_CURSES = ConfigBuildHandler.BUILDER.comment("Hide curse enchantments from the item tooltip.").define("Hide Curses", true);
        DISGUISE_ITEM = ConfigBuildHandler.BUILDER.comment("Hide enchantment glint and remove aqua color from name in case the item is solely enchanted with curses.").define("Disguise Item", true);
        SHIFT_SHOWS = ConfigBuildHandler.BUILDER.comment("Temporarily disable effects of the \"curses\" module when a shift key is pressed.").define("Shift Shows Curses", true);
        COLOR_NAME = ConfigBuildHandler.BUILDER.comment("Cursed items have a red name tag instead of an aqua one.").define("Color Item Name", true);
        DISGUISE_TAG = ConfigBuildHandler.BUILDER.comment("Remove one nbt tag entry in case the item is only enchanted with curses.").define("Disguise NBT Tag", false);
        AFFECT_BOOKS = ConfigBuildHandler.BUILDER.comment("Prevent curses from showing on enchanted books if they also hold other enchantments.").define("Affect Books", false);
        BUILDER.pop();
    }

    private static List<String> getEnchantmentList(Enchantment... enchantments) {

        return Stream.of(enchantments)
                .map(ForgeRegistries.ENCHANTMENTS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .collect(Collectors.toList());
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}