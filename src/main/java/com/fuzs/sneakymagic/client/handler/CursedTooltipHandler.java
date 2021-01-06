package com.fuzs.sneakymagic.client.handler;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.mixin.accessor.IItemAccessor;
import com.fuzs.sneakymagic.util.CurseMatcher;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class CursedTooltipHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemTooltip(final ItemTooltipEvent evt) {

        List<ITextComponent> tooltip = evt.getToolTip();
        ItemStack stack = evt.getItemStack();
        if (!stack.isEmpty() && (stack.isEnchanted() || ConfigBuildHandler.affectBooks && stack.getItem() == Items.ENCHANTED_BOOK)) {

            // check if item is cursed
            Collection<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();
            if (CurseMatcher.anyMatch(enchantments) && (!ConfigBuildHandler.shiftShows || !Screen.hasShiftDown()) && stack.hasTag()) {

                CompoundNBT tag = stack.getTag();
                this.modifyItemName(tooltip, stack, enchantments);
                assert tag != null;
                this.modifyCurses(tooltip, stack, enchantments, tag);
                if (evt.getFlags().isAdvanced()) {

                    this.modifyNbtTag(tooltip, enchantments, tag);
                }
            }
        }
    }

    private void modifyItemName(List<ITextComponent> tooltip, ItemStack stack, Collection<Enchantment> enchantments) {

        boolean disguise = ConfigBuildHandler.disguiseItem && stack.getItem() != Items.ENCHANTED_BOOK && CurseMatcher.allMatch(enchantments);
        if (disguise || ConfigBuildHandler.colorName) {

            Optional<StringTextComponent> nameComponent = tooltip.stream()
                    .filter(component -> component instanceof StringTextComponent)
                    .filter(component -> component.getUnformattedComponentText().contains(stack.getDisplayName().getUnformattedComponentText()))
                    .findFirst().map(component -> ((StringTextComponent) component));

            nameComponent.ifPresent(component -> component.mergeStyle(disguise ? ((IItemAccessor) stack.getItem()).getRarity().color : TextFormatting.RED));
        }
    }

    private void modifyCurses(List<ITextComponent> tooltip, ItemStack stack, Collection<Enchantment> enchantments, @Nonnull CompoundNBT tag) {

        boolean isHidingEnchantments = tag.contains("HideFlags", 99) && (tag.getInt("HideFlags") & ItemStack.TooltipDisplayFlags.ENCHANTMENTS.func_242397_a()) == 0;
        if (ConfigBuildHandler.hideCurses && (!isHidingEnchantments || stack.getItem() == Items.ENCHANTED_BOOK)) {

            if (stack.getItem() != Items.ENCHANTED_BOOK || !CurseMatcher.allMatch(enchantments)) {

                tooltip.removeIf(component -> this.getCursesAsTooltip(stack).contains(component));
            }
        }
    }

    private void modifyNbtTag(List<ITextComponent> tooltip, Collection<Enchantment> enchantments, @Nonnull CompoundNBT tag) {

        if (ConfigBuildHandler.disguiseTag && CurseMatcher.allMatch(enchantments)) {

            int index = tooltip.indexOf(new TranslationTextComponent("item.nbt_tags", tag.keySet().size()).mergeStyle(TextFormatting.DARK_GRAY));
            if (index != -1) {

                if (tag.keySet().size() > 1) {

                    tooltip.set(index, new TranslationTextComponent("item.nbt_tags", tag.keySet().size() - 1).mergeStyle(TextFormatting.DARK_GRAY));
                } else {

                    tooltip.remove(index);
                }
            }
        }
    }

    private List<ITextComponent> getCursesAsTooltip(ItemStack stack) {

        List<ITextComponent> curses = Lists.newArrayList();
        EnchantmentHelper.getEnchantments(stack).entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().isCurse())
                .forEach(entry -> curses.add(entry.getKey().getDisplayName(entry.getValue())));

        return curses;
    }

}
