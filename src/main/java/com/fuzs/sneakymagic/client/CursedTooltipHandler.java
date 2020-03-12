package com.fuzs.sneakymagic.client;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CursedTooltipHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemTooltip(final ItemTooltipEvent evt) {

        List<ITextComponent> tooltip = evt.getToolTip();
        ItemStack stack = evt.getItemStack();
        if (!stack.isEmpty() && (stack.isEnchanted() || ConfigBuildHandler.AFFECT_BOOKS.get())) {

            // check if item is cursed
            Map<Enchantment, Integer> enchants = ConfigBuildHandler.AFFECT_BOOKS.get() ? EnchantmentHelper.getEnchantments(stack) : EnchantmentHelper.func_226652_a_(stack.getEnchantmentTagList());
            if (enchants.keySet().stream().filter(Objects::nonNull).anyMatch(Enchantment::isCurse) && stack.hasTag() && stack.getTag() != null) {

                CompoundNBT tag = stack.getTag();
                boolean noShift = !(ConfigBuildHandler.SHIFT_SHOWS.get() && Screen.hasShiftDown());
                this.modifyItemName(tooltip, stack, noShift);
                this.modifyCurses(tooltip, stack, tag, noShift);
                this.modifyNbtTags(evt.getFlags().isAdvanced(), tooltip, enchants, tag, noShift);
            }
        }
    }

    private void modifyCurses(List<ITextComponent> tooltip, ItemStack stack, @Nonnull CompoundNBT tag, boolean noShift) {

        boolean flag = tag.contains("HideFlags", 99) && (tag.getInt("HideFlags") & 1) == 0;
        if (ConfigBuildHandler.HIDE_CURSES.get() && noShift && !flag) {

            tooltip.removeIf(line -> this.getCurseTooltips(stack).contains(line));
        }
    }

    private void modifyItemName(List<ITextComponent> tooltip, ItemStack stack, boolean noShift) {

        if (ConfigBuildHandler.COLOR_NAME.get() && noShift) {

            Optional<ITextComponent> name = tooltip.stream().filter(line -> line.equals(new StringTextComponent("").appendSibling(stack.getDisplayName()).applyTextStyle(stack.getRarity().color))).findFirst();
            name.ifPresent(line -> line.applyTextStyle(TextFormatting.RED));
        }
    }

    private void modifyNbtTags(boolean isAdvanced, List<ITextComponent> tooltip, Map<Enchantment, Integer> enchants, @Nonnull CompoundNBT tag, boolean noShift) {

        if (ConfigBuildHandler.DISGUISE_TAG.get() && isAdvanced && noShift && enchants.keySet().stream().filter(Objects::nonNull).allMatch(Enchantment::isCurse)) {

            int index = tooltip.indexOf(new TranslationTextComponent("item.nbt_tags", tag.keySet().size()).applyTextStyle(TextFormatting.DARK_GRAY));
            if (index != -1) {

                if (tag.keySet().size() > 1) {

                    tooltip.set(index, new TranslationTextComponent("item.nbt_tags", tag.keySet().size() - 1).applyTextStyle(TextFormatting.DARK_GRAY));
                } else {

                    tooltip.remove(index);
                }
            }
        }
    }

    private List<ITextComponent> getCurseTooltips(ItemStack stack) {

        List<ITextComponent> curses = Lists.newArrayList();
        EnchantmentHelper.getEnchantments(stack).entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().isCurse())
                .forEach(entry -> curses.add(entry.getKey().getDisplayName(entry.getValue())));

        return curses;
    }

}
