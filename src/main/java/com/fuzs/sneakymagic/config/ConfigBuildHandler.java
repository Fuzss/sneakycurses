package com.fuzs.sneakymagic.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    private static final EntryCollectionBuilder<Enchantment> ENCHANTMENT_COLLECTION_BUILDER = new EntryCollectionBuilder<>(ForgeRegistries.ENCHANTMENTS);
    private static final EntryCollectionBuilder<Item> ITEM_COLLECTION_BUILDER = new EntryCollectionBuilder<>(ForgeRegistries.ITEMS);

    // general
    public static boolean infinityMendingFix;
    public static boolean multishotPiercingFix;
    public static boolean protectionFix;
    public static boolean damageFix;
    public static boolean trueInfinity;
    public static boolean noProjectileResistance;
    // compatibility
    public static Set<Enchantment> swordEnchantments;
    public static Set<Enchantment> axeEnchantments;
    public static Set<Enchantment> tridentEnchantments;
    public static Set<Enchantment> bowEnchantments;
    public static Set<Enchantment> crossbowEnchantments;
    public static Set<Item> swordBlacklist;
    public static Set<Item> axeBlacklist;
    public static Set<Item> tridentBlacklist;
    public static Set<Item> bowBlacklist;
    public static Set<Item> crossbowBlacklist;
    // curses
    public static boolean hideCurses;
    public static boolean disguiseItem;
    public static boolean shiftShows;
    public static boolean colorName;
    public static boolean disguiseTag;
    public static boolean affectBooks;
    
    public static void setup(ModConfig.Type type) {
        
        ConfigManager.builder().createCategory("general", ConfigBuildHandler::setupGeneral, type);
        ConfigManager.builder().createCategory("compatibility", ConfigBuildHandler::setupCompatibility, type, "Only the enchantments included by default are guaranteed to work. While any modded enchantments or other vanilla enchantments can work, they are highly unlikely to do so.", "The blacklists for each item group are supposed to disable items which can be enchanted, but where the enchantments do not function as expected.", "Format for every entry is \"<namespace>:<path>\". Path may use asterisk as wildcard parameter. Changes made to this section of the config file require a restart to apply.");
        ConfigManager.builder().createCategory("curses", ConfigBuildHandler::setupCurses, type);
    }

    private static void setupGeneral(ForgeConfigSpec.Builder builder) {

        ConfigManager.get().registerEntry(builder.comment("Allows infinity and mending to be applied at the same time.").define("Infinity Mending Fix", true), v -> infinityMendingFix = v);
        ConfigManager.get().registerEntry(builder.comment("Allows multishot and piercing to be applied at the same time.").define("Multishot Piercing Fix", true), v -> multishotPiercingFix = v);
        ConfigManager.get().registerEntry(builder.comment("Allows different types of protection to be applied at the same time, just like in early versions of Minecraft 1.14.").define("Protection Fix", false), v -> protectionFix = v);
        ConfigManager.get().registerEntry(builder.comment("Allows different types of damage enchantments to be applied at the same time.").define("Damage Fix", false), v -> damageFix = v);
        ConfigManager.get().registerEntry(builder.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true), v -> trueInfinity = v);
        ConfigManager.get().registerEntry(builder.comment("Disables damage immunity when hit by a projectile. Makes it possible for entities to be hit by multiple projectiles at once.").define("No Projectile Immunity", true), v -> noProjectileResistance = v);
    }

    private static void setupCompatibility(ForgeConfigSpec.Builder builder) {

        String compatibility = "Additional enchantments to be made usable with ";
        String blacklist = " to be disabled from receiving additional enchantments.";
        ConfigManager.get().registerEntry(builder.comment(compatibility + "swords.").define("Sword Enchantments", getEnchantmentList(Enchantments.IMPALING)), v -> swordEnchantments = ENCHANTMENT_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment(compatibility + "axes.").define("Axe Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.IMPALING)), v -> axeEnchantments = ENCHANTMENT_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment(compatibility + "tridents.").define("Trident Enchantments", getEnchantmentList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.QUICK_CHARGE)), v -> tridentEnchantments = ENCHANTMENT_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment(compatibility + "bows.").define("Bow Enchantments", getEnchantmentList(Enchantments.PIERCING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE)), v -> bowEnchantments = ENCHANTMENT_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment(compatibility + "crossbows.").define("Crossbow Enchantments", getEnchantmentList(Enchantments.FLAME, Enchantments.PUNCH, Enchantments.POWER, Enchantments.INFINITY)), v -> crossbowEnchantments = ENCHANTMENT_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment("Swords" + blacklist).define("Sword Blacklist", new ArrayList<String>()), v -> swordBlacklist = ITEM_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment("Axes" + blacklist).define("Axe Blacklist", new ArrayList<String>()), v -> axeBlacklist = ITEM_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment("Tridents" + blacklist).define("Trident Blacklist", new ArrayList<String>()), v -> tridentBlacklist = ITEM_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment("Bows" + blacklist).define("Bow Blacklist", new ArrayList<String>()), v -> bowBlacklist = ITEM_COLLECTION_BUILDER.buildEntrySet(v));
        ConfigManager.get().registerEntry(builder.comment("Crossbows" + blacklist).define("Crossbow Blacklist", new ArrayList<String>()), v -> crossbowBlacklist = ITEM_COLLECTION_BUILDER.buildEntrySet(v));
    }

    private static void setupCurses(ForgeConfigSpec.Builder builder) {

        ConfigManager.get().registerEntry(builder.comment("Hide curse enchantments from the item tooltip.").define("Hide Curses", true), v -> hideCurses = v);
        ConfigManager.get().registerEntry(builder.comment("Hide enchantment glint and remove aqua color from name in case the item is solely enchanted with curses.").define("Disguise Item", true), v -> disguiseItem = v);
        ConfigManager.get().registerEntry(builder.comment("Temporarily disable effects of the \"curses\" module when a shift key is pressed.").define("Shift Shows Curses", true), v -> shiftShows = v);
        ConfigManager.get().registerEntry(builder.comment("Cursed items have a red name tag instead of an aqua one.").define("Color Item Name", true), v -> colorName = v);
        ConfigManager.get().registerEntry(builder.comment("Remove one nbt tag entry in case the item is only enchanted with curses.").define("Disguise NBT Tag", false), v -> disguiseTag = v);
        ConfigManager.get().registerEntry(builder.comment("Prevent curses from showing on enchanted books if they also hold other enchantments.").define("Affect Books", false), v -> affectBooks = v);
    }

    private static List<String> getEnchantmentList(Enchantment... enchantments) {

        return Stream.of(enchantments)
                .map(ForgeRegistries.ENCHANTMENTS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .collect(Collectors.toList());
    }

}