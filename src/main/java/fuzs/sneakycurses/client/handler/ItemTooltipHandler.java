package fuzs.sneakycurses.client.handler;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ItemTooltipHandler {
    @SubscribeEvent
    public void onItemTooltip(final ItemTooltipEvent evt) {
        if (!CurseMatcher.isAffected(evt.getItemStack())) return;
        this.dyeHoverName(evt.getToolTip());
        this.hideCurses(evt.getToolTip(), this.getCursesAsTooltip(evt.getItemStack()));
    }

    private void dyeHoverName(List<Component> tooltip) {
        if (!SneakyCurses.CONFIG.client().colorName) return;
        TextComponent hoverName = null;
        for (Component value : tooltip) {
            if (value instanceof TextComponent component && component.getText().equals("")) {
                hoverName = component;
                break;
            }
        }
        if (hoverName != null) {
            hoverName.withStyle(ChatFormatting.RED);
        }
    }

    private void hideCurses(List<Component> tooltip, List<Component> curses) {
        if (!SneakyCurses.CONFIG.client().hideCurses) return;
        tooltip.removeIf(curses::contains);
    }

    private List<Component> getCursesAsTooltip(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().isCurse())
                .map(entry -> entry.getKey().getFullname(entry.getValue()))
                .collect(Collectors.toList());
    }
}
