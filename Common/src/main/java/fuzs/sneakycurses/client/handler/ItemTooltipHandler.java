package fuzs.sneakycurses.client.handler;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.gui.screens.inventory.SimpleEnchantmentNames;
import fuzs.sneakycurses.config.ClientConfig;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class ItemTooltipHandler {
    private static int randomSeed;

    public static void setRandomSeed() {
        ItemTooltipHandler.randomSeed = SimpleEnchantmentNames.INSTANCE.random.nextInt();
    }

    public static void onItemTooltip(ItemStack stack, TooltipFlag context, List<Component> lines) {
        if (!CurseMatcher.isAffected(stack)) return;
        dyeHoverName(lines);
        if (!CurseRevealHandler.allCursesRevealed(stack)) {
            obfuscateCurses(lines);
        }
    }

    private static void dyeHoverName(List<Component> lines) {
        if (!SneakyCurses.CONFIG.get(ClientConfig.class).colorItemName) return;
        for (Component component : lines) {
            if (component instanceof MutableComponent mutableComponent) {
                TextColor color = mutableComponent.getStyle().getColor();
                if (color != null && color.equals(TextColor.fromLegacyFormat(ChatFormatting.AQUA))) {
                    mutableComponent.withStyle(ChatFormatting.RED);
                    return;
                }
            }
        }
    }

    private static void obfuscateCurses(List<Component> lines) {
        if (!SneakyCurses.CONFIG.get(ClientConfig.class).obfuscateCurses) return;
        // use this approach for compatibility with enchantment descriptions mod as this also matches their description key format
        ListIterator<Component> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof MutableComponent mutableComponent && mutableComponent.getContents() instanceof TranslatableContents translatableContents) {
                String translatableKey = translatableContents.getKey();
                Optional<Enchantment> enchantment = getEnchantmentByKey(translatableKey).filter(Enchantment::isCurse);
                if (enchantment.isPresent()) {
                    int enchantmentId = Registry.ENCHANTMENT.getId(enchantment.get());
                    SimpleEnchantmentNames.INSTANCE.initSeed(randomSeed + enchantmentId);
                    Component randomName = SimpleEnchantmentNames.INSTANCE.getRandomName(25);
                    iterator.set(Component.empty().append(randomName).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static Optional<Enchantment> getEnchantmentByKey(String translationKey) {
        if (translationKey.startsWith("enchantment")) {
            String[] enchantmentKey = translationKey.split("\\.");
            // this is the normal length for enchantment keys, other keys might be formatted similarly, but should have more than 3 elements (e.g. enchantment descriptions the corresponding mod)
            if (enchantmentKey.length == 3) {
                ResourceLocation name = new ResourceLocation(enchantmentKey[1], enchantmentKey[2]);
                if (Registry.ENCHANTMENT.containsKey(name)) {
                    return Optional.ofNullable(Registry.ENCHANTMENT.get(name));
                }
            }
        }
        return Optional.empty();
    }
}
