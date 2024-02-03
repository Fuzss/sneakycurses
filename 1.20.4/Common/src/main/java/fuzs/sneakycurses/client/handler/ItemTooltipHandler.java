package fuzs.sneakycurses.client.handler;

import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.gui.v2.screen.ScreenHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.util.ComponentHelper;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class ItemTooltipHandler {
    private static final Random RANDOM = new Random();

    private static int currentScreenSeed;

    public static void onAfterInit(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, ScreenEvents.ConsumingOperator<AbstractWidget> addWidget, ScreenEvents.ConsumingOperator<AbstractWidget> removeWidget) {
        setFreshSeed();
    }

    private static void setFreshSeed() {
        currentScreenSeed = RANDOM.nextInt();
    }

    public static void onItemTooltip(ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag context) {
        if (context.isCreative()) return;
        if (!SneakyCurses.CONFIG.getHolder(ServerConfig.class).isAvailable() ||
                !SneakyCurses.CONFIG.get(ServerConfig.class).obfuscateCurses) return;
        if (!isAffected(player, itemStack)) return;
        ListIterator<Component> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getContents() instanceof TranslatableContents contents &&
                    contents.getKey().startsWith("enchantment.")) {
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
                        Component component = getLoreForWidth(Minecraft.getInstance().font);
                        iterator.set(Component.empty().append(component).withStyle(ChatFormatting.RED));
                    } else {
                        // remove descriptions from the enchantment descriptions mod, this matches their format
                        iterator.remove();
                    }
                }
            }
        }
    }

    private static boolean isAffected(@Nullable Player player, ItemStack itemStack) {
        if (player != null && player.level().isClientSide) {
            if (itemStack.isEmpty()) {
                return false;
            }
            // show when holding shift in creative mode
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode.hasInfiniteItems() &&
                    Screen.hasShiftDown() &&
                    SneakyCurses.CONFIG.get(ServerConfig.class).shiftShows) {
                return false;
            } else if (itemStack.getItem() instanceof EnchantedBookItem &&
                    !SneakyCurses.CONFIG.get(ServerConfig.class).affectBooks) {
                return false;
            }
            // don't show in anvil output slot, since it would reveal curses without actually having to apply the operation
            if (minecraft.screen instanceof AnvilScreen screen) {
                Slot hoveredSlot = ScreenHelper.INSTANCE.getHoveredSlot(screen);
                if (hoveredSlot != null &&
                        screen.getMenu().getResultSlot() == hoveredSlot.index &&
                        hoveredSlot.getItem() == itemStack) {
                    Slot inputSlot = screen.getMenu().getSlot(0);
                    if (!CurseRevealHandler.allCursesRevealed(inputSlot.getItem())) {
                        return true;
                    }
                }
            }
            return CurseRevealHandler.isAffected(itemStack) && !CurseRevealHandler.allCursesRevealed(itemStack);
        }
        return false;
    }

    private static void initSeed(long seed) {
        RANDOM.setSeed(seed);
        EnchantmentNames.getInstance().initSeed(seed);
    }

    private static Component getLoreForWidth(Font font) {
        double maxWidth = RANDOM.nextGaussian(100, 20);
        FormattedText formattedText = EnchantmentNames.getInstance().getRandomName(font, (int) maxWidth);
        return ComponentHelper.toComponent(formattedText);
    }
}
