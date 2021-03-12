package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {

        super(type, worldIn);
    }

    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;distanceWalkedModified:F"), to = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspectModifier(Lnet/minecraft/entity/LivingEntity;)I")))
    public Item getFakeSword(ItemStack itemStack) {

        // make sweeping edge work not just on swords, therefore this simply needs to be any instance of SwordItem
        ImprovementsElement element = (ImprovementsElement) SneakyMagic.ENCHANTMENT_IMPROVEMENTS;
        if (Enchantments.SWEEPING.canApply(itemStack) && (!element.isEnabled() || !element.requireSweepingEdge || EnchantmentHelper.getSweepingDamageRatio(this) != 0.0F)) {

            return Items.IRON_SWORD;
        }

        return Items.IRON_PICKAXE;
    }

    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getModifierForCreature(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/CreatureAttribute;)F", ordinal = 0))
    public float getModifierForCreature(ItemStack stack, CreatureAttribute creatureAttribute, Entity targetEntity) {

        // make impaling work on mobs when in contact with water or rain
        return ImprovementsElement.getModifierForCreature(stack, creatureAttribute, (LivingEntity) targetEntity);
    }

}
