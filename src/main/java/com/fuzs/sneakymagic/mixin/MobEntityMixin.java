package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.common.element.ImprovementsElement;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {

        super(type, worldIn);
    }

    @Redirect(method = "attackEntityAsMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getModifierForCreature(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/CreatureAttribute;)F", ordinal = 0))
    public float getModifierForCreature(ItemStack stack, CreatureAttribute creatureAttribute, Entity entityIn) {

        return ImprovementsElement.getModifierForCreature(stack, creatureAttribute, (LivingEntity) entityIn);
    }

}
