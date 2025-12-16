package fuzs.sneakycurses.client.handler;

import com.google.common.collect.MapMaker;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ItemTooltipHandler {
    private static final Map<Enchantment, Integer> ENCHANTMENT_IDS = new MapMaker().weakKeys()
            .concurrencyLevel(1)
            .makeMap();
    private static final Random RANDOM = new Random();

    private static int currentScreenSeed;

    public static void onAfterInit(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, UnaryOperator<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        setFreshSeed();
    }

    private static void setFreshSeed() {
        currentScreenSeed = RANDOM.nextInt();
    }

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (tooltipFlag.isCreative() || tooltipContext.registries() == null) return;
        if (!SneakyCurses.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !SneakyCurses.CONFIG.get(ServerConfig.class).obfuscateCurses) {
            return;
        }
        if (!isAffected(player, itemStack)) return;
        ListIterator<Component> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getContents() instanceof TranslatableContents contents && contents.getKey()
                    .startsWith("enchantment.")) {
                String[] enchantmentKey = contents.getKey().split("\\.");
                Holder<Enchantment> enchantment = null;
                if (enchantmentKey.length >= 3) {
                    HolderLookup.RegistryLookup<Enchantment> enchantments = tooltipContext.registries()
                            .lookupOrThrow(Registries.ENCHANTMENT);
                    Identifier identifier = Identifier.fromNamespaceAndPath(enchantmentKey[1],
                            enchantmentKey[2]);
                    enchantment = enchantments.get(ResourceKey.create(Registries.ENCHANTMENT, identifier))
                            .orElse(null);
                }
                if (enchantment != null && enchantment.is(EnchantmentTags.CURSE)) {
                    if (enchantmentKey.length == 3) {
                        int enchantmentId = ENCHANTMENT_IDS.computeIfAbsent(enchantment.value(), $ -> RANDOM.nextInt());
                        initSeed(currentScreenSeed + enchantmentId);
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
        if (player != null && player.level().isClientSide()) {
            if (itemStack.isEmpty()) {
                return false;
            }
            // show when holding shift in creative mode
            if (player.hasInfiniteMaterials() && CommonHelper.hasShiftDown()
                    && SneakyCurses.CONFIG.get(ServerConfig.class).shiftShows) {
                return false;
            } else if (itemStack.is(Items.ENCHANTED_BOOK) && !SneakyCurses.CONFIG.get(ServerConfig.class).affectBooks) {
                return false;
            }
            // don't show in anvil output slot, since it would reveal curses without actually having to apply the operation
            if (Minecraft.getInstance().screen instanceof AnvilScreen screen) {
                Slot hoveredSlot = screen.hoveredSlot;
                if (hoveredSlot != null && screen.getMenu().getResultSlot() == hoveredSlot.index
                        && hoveredSlot.getItem() == itemStack) {
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
        return ComponentHelper.getAsComponent(formattedText);
    }
}
