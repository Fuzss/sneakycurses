package com.fuzs.sneakymagic.mixin.accessor;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrowEntity.class)
public interface IAbstractArrowEntityAccessor {

    @Invoker
    ItemStack callGetArrowStack();

    @Accessor
    IntOpenHashSet getPiercedEntities();

    @Accessor
    void setPiercedEntities(IntOpenHashSet piercedEntities);

    @Accessor
    int getKnockbackStrength();

}
