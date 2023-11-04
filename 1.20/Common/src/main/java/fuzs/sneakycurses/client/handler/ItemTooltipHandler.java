package fuzs.sneakycurses.client.handler;

import fuzs.puzzleslib.api.client.event.v1.ScreenEvents;
import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;

public class ItemTooltipHandler {
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
    private static final Style ALT_FONT_STYLE = Style.EMPTY.withFont(new ResourceLocation("alt"));
    private static final String[] ENCHANTMENT_LORE = new String[]{"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette"};

    private static int currentScreenSeed;

    public static void onAfterInit(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, ScreenEvents.ConsumingOperator<AbstractWidget> addWidget, ScreenEvents.ConsumingOperator<AbstractWidget> removeWidget) {
        setFreshSeed();
    }

    public static void onItemTooltip(ItemStack stack, @Nullable Player player, List<Component> lines, TooltipFlag context) {
        if (!SneakyCurses.CONFIG.get(ServerConfig.class).obfuscateCurses) return;
        if (!isAffected(player, stack)) return;
        ListIterator<Component> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getContents() instanceof TranslatableContents contents && contents.getKey().startsWith("enchantment.")) {
                String[] enchantmentKey = contents.getKey().split("\\.");
                Enchantment enchantment = null;
                if (enchantmentKey.length >= 3) {
                    ResourceLocation resourceLocation = new ResourceLocation(enchantmentKey[1], enchantmentKey[2]);
                    if (BuiltInRegistries.ENCHANTMENT.containsKey(resourceLocation)) {
                        enchantment = BuiltInRegistries.ENCHANTMENT.get(resourceLocation);
                    }
                }
                if (enchantment != null && enchantment.isCurse()) {
                    if (enchantmentKey.length == 3) {
                        initSeed(currentScreenSeed + BuiltInRegistries.ENCHANTMENT.getId(enchantment));
                        Component component = getLoreForWidth();
                        iterator.set(Component.empty().append(component).withStyle(ChatFormatting.RED));
                    } else {
                        // remove descriptions from the enchantment descriptions mod, this matches their format
                        iterator.remove();
                    }
                }
            }
        }
    }

    private static Component getLoreForWidth() {
        int maxWidth = RANDOM_SOURCE.nextInt(16, 24);
        StringBuilder builder = new StringBuilder();
        while (builder.length() <= maxWidth) {
            if (!builder.isEmpty()) builder.append(" ");
            builder.append(Util.getRandom(ENCHANTMENT_LORE, RANDOM_SOURCE));
        }
        if (builder.lastIndexOf(" ") != -1) {
            builder.delete(builder.lastIndexOf(" "), builder.length());
        }
        return Component.literal(builder.toString()).withStyle(ALT_FONT_STYLE);
    }

    private static void setFreshSeed() {
        currentScreenSeed = RANDOM_SOURCE.nextInt();
    }

    private static void initSeed(long seed) {
        RANDOM_SOURCE.setSeed(seed);
    }

    private static boolean isAffected(@Nullable Player player, ItemStack itemStack) {
        if (player != null && player.level().isClientSide) {
            if (itemStack.isEmpty()) {
                return false;
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode.hasInfiniteItems() && Screen.hasShiftDown() && SneakyCurses.CONFIG.get(ServerConfig.class).shiftShows) {
                return false;
            } else if (itemStack.getItem() instanceof EnchantedBookItem) {
                if (!SneakyCurses.CONFIG.get(ServerConfig.class).affectBooks) {
                    return false;
                } else if (minecraft.screen instanceof CreativeModeInventoryScreen screen && !screen.isInventoryOpen()) {
                    return false;
                }
            }
            if (minecraft.screen instanceof AnvilScreen screen) {
                Slot hoveredSlot = ScreenHelper.INSTANCE.getHoveredSlot(screen);
                if (hoveredSlot != null && screen.getMenu().getResultSlot() == hoveredSlot.index && hoveredSlot.getItem() == itemStack) {
                    Slot inputSlot = screen.getMenu().getSlot(0);
                    if (!CurseRevealHandler.allCursesRevealed(inputSlot.getItem())) {
                        return true;
                    }
                }
            }
        }
        return CurseRevealHandler.isAffected(itemStack) && !CurseRevealHandler.allCursesRevealed(itemStack);
    }
}
