package com.fuzs.sneakymagic.mixin.accessor;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface IItemAccessor {

    @Accessor
    Rarity getRarity();

}
