package com.fuzs.sneakycurses.asm;

import com.fuzs.sneakycurses.handler.ConfigHandler;
import com.fuzs.sneakycurses.helper.CurseHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public final class Hooks {

    private static ItemStack targetStack = ItemStack.EMPTY;

    public static void setTargetStack(ItemStack stack) {
        targetStack = stack;
    }

    public static void setTargetStack(EntityLivingBase entity, EntityEquipmentSlot slot) {
        setTargetStack(entity.getItemStackFromSlot(slot));
    }

    public static int applyColor(int original) {

        boolean flag = original != -8372020 && ConfigHandler.a51QuarkCompat;
        return leaveAsIs() || flag ? original : getColor();

    }

    public static void applyColor() {

        if (leaveAsIs() || CurseHelper.hasQuarkRune(targetStack)) {
            return;
        }

        int color = getColor();
        float a = (color >> 24 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;

        GlStateManager.color(r, g, b, a);

    }

    public static int getColor() {

        int color = ItemDye.DYE_COLORS[ConfigHandler.a4CurseGlintColor.getDyeDamage()];

        return (alphaValue() << 24) | color;

    }

    private static int alphaValue() {
        return ConfigHandler.a2HideGlint && CurseHelper.onlyCurses(targetStack)
                && !CurseHelper.isItemBlacklisted(targetStack) ? 0 : 255;
    }

    private static boolean leaveAsIs() {
        return targetStack.isEmpty() || !CurseHelper.isCursed(targetStack) || ConfigHandler.a4CurseGlintColor.getDyeDamage() == -1;
    }

}
