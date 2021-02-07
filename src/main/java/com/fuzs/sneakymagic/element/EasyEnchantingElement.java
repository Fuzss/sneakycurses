package com.fuzs.sneakymagic.element;

import com.fuzs.puzzleslib_sm.element.extension.ClientExtensibleElement;
import com.fuzs.sneakymagic.client.element.EasyEnchantingExtension;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ForgeConfigSpec;

public class EasyEnchantingElement extends ClientExtensibleElement<EasyEnchantingExtension> {

    public boolean itemsStay;
    public boolean rerollEnchantments;

    public EasyEnchantingElement() {

        super(element -> new EasyEnchantingExtension((EasyEnchantingElement) element));
    }

    @Override
    public String getDescription() {

        return "Small improvements for making working with an enchantment table more fun.";
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Inventory contents stay in their slot after closing the enchanting screen. Also makes hoppers able to input and output items.").define("Contents Stay", true), v -> this.itemsStay = v);
        addToConfig(builder.comment("Reroll possible enchantments in an enchanting table every time an item is placed into the enchanting slot.").define("Reroll Enchantments", true), v -> this.rerollEnchantments = v);
    }

    public static Rarity getFutureRarity(ItemStack stack) {

        if (stack.getItem() == Items.BOOK) {

            return Rarity.UNCOMMON;
        } else {

            switch (stack.getRarity()) {

                case COMMON:
                case UNCOMMON:

                    return Rarity.RARE;
                case RARE:

                    return Rarity.EPIC;
                case EPIC:
                default:

                    return stack.getRarity();
            }
        }
    }

}
