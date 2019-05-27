package com.fuzs.sneakycurses.helper;

import net.minecraft.util.text.TextFormatting;

/**
 * Resembles {@link net.minecraft.item.EnumDyeColor} with accessible text formatting codes and a default element
 */
@SuppressWarnings("unused")
public enum EnumConfigColour {

    WHITE(15, "white", TextFormatting.WHITE),
    ORANGE(14, "orange", TextFormatting.GOLD),
    MAGENTA(13, "magenta", TextFormatting.AQUA),
    LIGHT_BLUE(12, "light_blue", TextFormatting.BLUE),
    YELLOW(11, "yellow", TextFormatting.YELLOW),
    LIME(10, "lime", TextFormatting.GREEN),
    PINK(9, "pink", TextFormatting.LIGHT_PURPLE),
    GRAY(8, "gray", TextFormatting.DARK_GRAY),
    SILVER(7, "silver", TextFormatting.GRAY),
    CYAN(6, "cyan", TextFormatting.DARK_AQUA),
    PURPLE(5, "purple", TextFormatting.DARK_PURPLE),
    BLUE(4, "blue", TextFormatting.DARK_BLUE),
    BROWN(3, "brown", TextFormatting.RED),
    GREEN(2, "green", TextFormatting.DARK_GREEN),
    RED(1, "red", TextFormatting.DARK_RED),
    BLACK(0, "black", TextFormatting.BLACK),
    DEFAULT(-1, "default", TextFormatting.RESET);

    private final int dyeDamage;
    private final String unlocalizedName;
    private final TextFormatting chatColor;

    EnumConfigColour(int dyeDamageIn, String unlocalizedNameIn, TextFormatting chatColorIn) {
        this.dyeDamage = dyeDamageIn;
        this.unlocalizedName =unlocalizedNameIn;
        this.chatColor = chatColorIn;
    }

    public int getDyeDamage()
    {
        return this.dyeDamage;
    }

    public String toString()
    {
        return this.unlocalizedName;
    }

    public TextFormatting getChatColor()
    {
        return this.chatColor;
    }

}
