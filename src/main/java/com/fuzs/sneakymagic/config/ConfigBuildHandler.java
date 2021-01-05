package com.fuzs.sneakymagic.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    // general
    public static boolean infinityMendingFix;
    public static boolean multishotPiercingFix;
    public static boolean protectionFix;
    public static boolean damageFix;
    public static boolean trueInfinity;
    public static boolean noProjectileResistance;
    // compatibility
    public static List<String> swordEnchantments;
    public static List<String> axeEnchantments;
    public static List<String> tridentEnchantments;
    public static List<String> bowEnchantments;
    public static List<String> crossbowEnchantments;
    // curses
    public static boolean hideCurses;
    public static boolean disguiseItem;
    public static boolean shiftShows;
    public static boolean colorName;
    public static boolean disguiseTag;
    public static boolean affectBooks;
    
    public static void setup(ForgeConfigSpec.Builder builder) {
        
        createCategory("general", builder, ConfigBuildHandler::setupGeneral);
        createCategory("compatibility", builder, ConfigBuildHandler::setupCompatibility, "Changes made to this section of the config file require a restart to apply.", "Format for every entry is \"<namespace>:<path>\". Path may use asterisk as wildcard parameter.");
        createCategory("curses", builder, ConfigBuildHandler::setupCurses);
    }

    private static void setupGeneral(ForgeConfigSpec.Builder builder) {

        ConfigManager.registerEntry(builder.comment("Allows infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true), v -> infinityMendingFix = v);
        ConfigManager.registerEntry(builder.comment("Allows multishot and piercing to be applied at the same time.").define("Multishot Piercing Fix", true), v -> multishotPiercingFix = v);
        ConfigManager.registerEntry(builder.comment("Allows different types of protection to be applied at the same time, just like in early versions of Minecraft 1.14.").define("Protection Fix", false), v -> protectionFix = v);
        ConfigManager.registerEntry(builder.comment("Allows different types of damage enchantments to be applied at the same time.").define("Damage Fix", false), v -> damageFix = v);
        ConfigManager.registerEntry(builder.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true), v -> trueInfinity = v);
        ConfigManager.registerEntry(builder.comment("Disables damage immunity when hit by a projectile. Makes it possible for entities to be hit by multiple projectiles at once.").define("No Projectile Immunity", true), v -> noProjectileResistance = v);
    }

    private static void setupCompatibility(ForgeConfigSpec.Builder builder) {

        String compatibility = "Additional enchantments to be made applicable to ";
        ConfigManager.registerEntry(builder.comment(compatibility + "swords.").define("Sword Enchantments", getEnchantmentList(Enchantments.IMPALING)), v -> swordEnchantments = v);
        ConfigManager.registerEntry(builder.comment(compatibility + "axes.").define("Axe Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.IMPALING)), v -> axeEnchantments = v);
        ConfigManager.registerEntry(builder.comment(compatibility + "tridents.").define("Trident Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.QUICK_CHARGE)), v -> tridentEnchantments = v);
        ConfigManager.registerEntry(builder.comment(compatibility + "bows.").define("Bow Enchantments", getEnchantmentList(Enchantments.PIERCING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE)), v -> bowEnchantments = v);
        ConfigManager.registerEntry(builder.comment(compatibility + "crossbows.").define("Crossbow Enchantments", getEnchantmentList(Enchantments.FLAME, Enchantments.PUNCH, Enchantments.POWER, Enchantments.INFINITY)), v -> crossbowEnchantments = v);
    }

    private static void setupCurses(ForgeConfigSpec.Builder builder) {

        ConfigManager.registerEntry(builder.comment("Hide curse enchantments from the item tooltip.").define("Hide Curses", true), v -> hideCurses = v);
        ConfigManager.registerEntry(builder.comment("Hide enchantment glint and remove aqua color from name in case the item is solely enchanted with curses.").define("Disguise Item", true), v -> disguiseItem = v);
        ConfigManager.registerEntry(builder.comment("Temporarily disable effects of the \"curses\" module when a shift key is pressed.").define("Shift Shows Curses", true), v -> shiftShows = v);
        ConfigManager.registerEntry(builder.comment("Cursed items have a red name tag instead of an aqua one.").define("Color Item Name", true), v -> colorName = v);
        ConfigManager.registerEntry(builder.comment("Remove one nbt tag entry in case the item is only enchanted with curses.").define("Disguise NBT Tag", false), v -> disguiseTag = v);
        ConfigManager.registerEntry(builder.comment("Prevent curses from showing on enchanted books if they also hold other enchantments.").define("Affect Books", false), v -> affectBooks = v);
    }

    private static void createCategory(String name, ForgeConfigSpec.Builder builder, Consumer<ForgeConfigSpec.Builder> options, String... comments) {

        if (comments.length != 0) {

            builder.comment(comments);
        }

        builder.push(name);
        options.accept(builder);
        builder.pop();
    }

    private static List<String> getEnchantmentList(Enchantment... enchantments) {

        return Stream.of(enchantments)
                .map(ForgeRegistries.ENCHANTMENTS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .collect(Collectors.toList());
    }

}