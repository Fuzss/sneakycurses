package com.fuzs.sneakymagic.client.element;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.IClientElement;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SneakyCursesElement extends AbstractElement implements IClientElement {
    
    private boolean hideCurses;
    public boolean disguiseItem;
    private boolean shiftShows;
    public boolean colorName;
    public boolean affectBooks;
    
    @Override
    public String getDescription() {

        return "Makes curse enchantments no longer show right away, making it a lot harder to know when an item is cursed.";
    }

    @Override
    public void setupClient() {
        
        this.addListener(this::onItemTooltip);
    }

    @Override
    public void setupClientConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Hide curse enchantments from the item tooltip.").define("Hide Curses", true), v -> hideCurses = v);
        addToConfig(builder.comment("Hide enchantment glint and remove aqua color from name in case the item is solely enchanted with curses.").define("Disguise Item", true), v -> disguiseItem = v);
        addToConfig(builder.comment("Temporarily disable effects of the \"curses\" module when a shift key is pressed.").define("Shift Shows Curses", true), v -> shiftShows = v);
        addToConfig(builder.comment("Cursed items have a red name tag instead of an aqua one.").define("Color Item Name", true), v -> colorName = v);
        addToConfig(builder.comment("Prevent curses from showing on enchanted books if they also hold other enchantments.").define("Affect Books", false), v -> affectBooks = v);
    }

    private void onItemTooltip(final ItemTooltipEvent evt) {

        List<ITextComponent> tooltip = evt.getToolTip();
        ItemStack stack = evt.getItemStack();
        if (!stack.isEmpty() && (stack.isEnchanted() || this.affectBooks && stack.getItem() == Items.ENCHANTED_BOOK)) {

            // check if item is cursed
            Collection<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();
            if (CurseMatcher.anyMatch(enchantments) && (!this.shiftShows || !Screen.hasShiftDown()) && stack.hasTag()) {

                CompoundNBT tag = stack.getTag();
                this.modifyItemName(tooltip, stack);
                assert tag != null;
                this.modifyCurses(tooltip, stack, enchantments, tag);
            }
        }
    }

    private void modifyItemName(List<ITextComponent> tooltip, ItemStack stack) {

        if (this.colorName) {

            Optional<StringTextComponent> nameComponent = tooltip.stream()
                    .filter(component -> component instanceof StringTextComponent)
                    .filter(component -> component.getUnformattedComponentText().contains(stack.getDisplayName().getUnformattedComponentText()))
                    .findFirst().map(component -> ((StringTextComponent) component));

            boolean handleBook = !this.affectBooks && stack.getItem() == Items.ENCHANTED_BOOK;
            nameComponent.ifPresent(component -> component.mergeStyle(handleBook ? ((IItemAccessor) stack.getItem()).getRarity().color : TextFormatting.RED));
        }
    }

    private void modifyCurses(List<ITextComponent> tooltip, ItemStack stack, Collection<Enchantment> enchantments, @Nonnull CompoundNBT tag) {

        boolean isHidingEnchantments = tag.contains("HideFlags", 99) && (tag.getInt("HideFlags") & ItemStack.TooltipDisplayFlags.ENCHANTMENTS.func_242397_a()) == 0;
        if (this.hideCurses && (!isHidingEnchantments || stack.getItem() == Items.ENCHANTED_BOOK)) {

            if (stack.getItem() != Items.ENCHANTED_BOOK || !CurseMatcher.allMatch(enchantments)) {

                tooltip.removeIf(component -> this.getCursesAsTooltip(stack).contains(component));
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
