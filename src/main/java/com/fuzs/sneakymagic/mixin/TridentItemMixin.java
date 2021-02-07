package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.AnvilTweaksElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item {

    public TridentItemMixin(Properties properties) {

        super(properties);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {

        AnvilTweaksElement element = SneakyMagicElements.getAs(SneakyMagicElements.ANVIL_TWEAKS);
        if (element.isEnabled() && element.repairTridentWithPrismarine) {

            return repair.getItem().isIn(Tags.Items.DUSTS_PRISMARINE);
        }

        return false;
    }

}
