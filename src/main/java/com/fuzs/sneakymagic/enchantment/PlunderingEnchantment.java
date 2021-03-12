package com.fuzs.sneakymagic.enchantment;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.element.CompatibilityElement;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.LootBonusEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

@SuppressWarnings("NullableProblems")
public class PlunderingEnchantment extends LootBonusEnchantment {

    public PlunderingEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {

        super(rarityIn, typeIn, slots);
    }

    private boolean isEnabled() {

        CompatibilityElement element = (CompatibilityElement) SneakyMagic.ENCHANTMENT_COMPATIBILITY;
        return element.isEnabled() && element.plundering;
    }

    @Override
    public boolean canApply(ItemStack stack) {

        return this.isEnabled() && super.canApply(stack);
    }

    @Override
    public boolean canVillagerTrade() {

        return this.isEnabled() && super.canVillagerTrade();
    }

    @Override
    public boolean canGenerateInLoot() {

        return this.isEnabled() && super.canGenerateInLoot();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {

        return this.isEnabled() && super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public boolean isAllowedOnBooks() {

        return this.isEnabled() && super.isAllowedOnBooks();
    }

}
