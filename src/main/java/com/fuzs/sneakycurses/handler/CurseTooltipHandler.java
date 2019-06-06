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

@SuppressWarnings({"deprecation", "unused"})
public class CurseTooltipHandler {

    @SubscribeEvent
    public void makeTooltip(ItemTooltipEvent evt) {

        ItemStack stack = evt.getItemStack();
        if(!stack.isEmpty() && stack.isItemEnchanted()) {

            List<String> tooltip = evt.getToolTip();

            // colorise cursed item name
            if (CurseHelper.isCursed(stack)) {
                if (ConfigHandler.a5NameColor.getDyeDamage() != -1 || CurseHelper.onlyCurses(stack)) {
                    String s3 = ConfigHandler.a5NameColor.getChatColor() + tooltip.get(0);
                    tooltip.set(0, s3);
                }
            }

            // modify nbt tag
            if (ConfigHandler.hideNBT && CurseHelper.onlyCurses(stack)) {
                if (evt.getFlags().isAdvanced() && stack.getTagCompound() != null) {
                    // using the deprecated method as we want to find the exact occurrence
                    String s = TextFormatting.DARK_GRAY + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("item.nbt_tags", stack.getTagCompound().getKeySet().size());
                    String s1 = (new TextComponentTranslation("item.nbt_tags", stack.getTagCompound().getKeySet().size() - 1))
                            .setStyle((new Style()).setColor(TextFormatting.DARK_GRAY)).getFormattedText();
                    if (stack.getTagCompound().getKeySet().size() > 1) {
                        tooltip.set(tooltip.indexOf(s), s1);
                    } else {
                        tooltip.remove(s);
                    }
                }
            }

            // remove curse enchantment entries from here
            if (!ConfigHandler.a1HideCurses || (ConfigHandler.a21ShiftCurses && GuiScreen.isShiftKeyDown())) {
                return;
            }

            List<String> list = new ArrayList<>();
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            for (int j = 0; j < nbttaglist.tagCount(); ++j)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                int k = nbttagcompound.getShort("id");
                int l = nbttagcompound.getShort("lvl");
                Enchantment enchantment = Enchantment.getEnchantmentByID(k);

                if (enchantment != null && enchantment.isCurse())
                {
                    list.add(enchantment.getTranslatedName(l));
                }
            }

            if (!list.isEmpty()) {
                tooltip.removeAll(list);
            }
        }

    }

}
