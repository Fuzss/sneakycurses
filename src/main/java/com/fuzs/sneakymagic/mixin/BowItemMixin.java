package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.element.CompatibilityElement;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Redirect(method = "onPlayerStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;customeArrow(Lnet/minecraft/entity/projectile/AbstractArrowEntity;)Lnet/minecraft/entity/projectile/AbstractArrowEntity;", remap = false))
    public AbstractArrowEntity customArrow(BowItem bow, AbstractArrowEntity arrow, ItemStack stack) {

        arrow = bow.customeArrow(arrow);
        CompatibilityElement.applyPiercingEnchantment(arrow, stack);
        CompatibilityElement.applyPlunderingEnchantment(arrow, stack);

        return arrow;
    }

}
