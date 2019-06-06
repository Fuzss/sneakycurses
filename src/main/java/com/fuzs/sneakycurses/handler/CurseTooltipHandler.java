package com.fuzs.sneakycurses.handler;

import com.fuzs.sneakycurses.helper.CurseHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CurseTooltipHandler {

    @SubscribeEvent
    public void makeTooltip(ItemTooltipEvent evt) {

        ItemStack stack = evt.getItemStack();
        if(!stack.isEmpty() && stack.isItemEnchanted()) {

            List<String> tooltip = evt.getToolTip(), tooltipPlain;
            tooltipPlain = tooltip.stream().map(TextFormatting::getTextWithoutFormattingCodes).collect(Collectors.toList());

            // colourise cursed item name
            if (CurseHelper.isCursed(stack)) {

                if (ConfigHandler.a5NameColor.getDyeDamage() != -1 || CurseHelper.onlyCurses(stack)) {

                    List<String> name = tooltipPlain.stream().filter(it -> it.contains(stack.getDisplayName())).collect(Collectors.toList());
                    int index = -1;

                    if (!name.isEmpty()) {
                        index = tooltipPlain.indexOf(name.get(0));
                    }

                    if (index != -1) {
                        tooltip.set(index, ConfigHandler.a5NameColor.getChatColor() + tooltip.get(index));
                    }

                }

            }

            // modify nbt tag
            if (ConfigHandler.hideNBT && CurseHelper.onlyCurses(stack)) {

                if (evt.getFlags().isAdvanced() && stack.getTagCompound() != null) {

                    String s1 = new TextComponentTranslation("item.nbt_tags", stack.getTagCompound().getKeySet().size()).getUnformattedText();
                    int index = tooltipPlain.indexOf(s1);

                    if (index != -1) {

                        if (stack.getTagCompound().getKeySet().size() > 1) {

                            String s2 = new TextComponentTranslation("item.nbt_tags", stack.getTagCompound().getKeySet().size() - 1)
                                    .setStyle(new Style().setColor(TextFormatting.DARK_GRAY)).getFormattedText();
                            tooltip.set(tooltipPlain.indexOf(s1), s2);

                        } else {

                            tooltip.remove(tooltipPlain.indexOf(s1));

                        }

                    }

                }

            }

            // remove curse enchantment entries from here
            if (!ConfigHandler.a1HideCurses || (ConfigHandler.a21ShiftCurses && GuiScreen.isShiftKeyDown())) {
                return;
            }

            List<String> list = new ArrayList<>();
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            for (int j = 0; j < nbttaglist.tagCount(); ++j) {

                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                int k = nbttagcompound.getShort("id");
                int l = nbttagcompound.getShort("lvl");
                Enchantment enchantment = Enchantment.getEnchantmentByID(k);

                if (enchantment != null && enchantment.isCurse()) {
                    list.add(TextFormatting.getTextWithoutFormattingCodes(enchantment.getTranslatedName(l)));
                }

            }

            List<Integer> indices = new ArrayList<>();
            list.forEach(it -> {

                int j = tooltipPlain.indexOf(it);
                if (j != -1) {
                    indices.add(0, j);
                }

            });

            for (int i : indices) {

                if (i < tooltip.size()) {
                    tooltip.remove(i);
                }

            }

        }

    }

}
