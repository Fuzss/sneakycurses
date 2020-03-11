package com.fuzs.sneakymagic.common;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CompatibilityHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onArrowLoose(final ArrowLooseEvent evt) {

        // multishot enchantment for bows
        ItemStack stack = evt.getBow();
        if (evt.hasAmmo() && EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) {

            float velocity = BowItem.getArrowVelocity(evt.getCharge());
            if ((double) velocity >= 0.1) {

                PlayerEntity playerentity = evt.getPlayer();
                ItemStack itemstack = playerentity.findAmmo(stack);
                boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

                for (int i = 0; i < 2; i++) {

                    AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(evt.getWorld(), itemstack, playerentity);
                    abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, velocity * 3.0F, 10.0F);
                    addArrowEnchantments(abstractarrowentity, stack);
                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    evt.getWorld().addEntity(abstractarrowentity);
                }

            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemUseTick(final LivingEntityUseItemEvent.Tick evt) {

        if (evt.getItem().getItem() instanceof BowItem && evt.getItem().getUseDuration() - evt.getDuration() < 20) {

            // quick charge enchantment for bows
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, evt.getItem());
            evt.setDuration(evt.getDuration() - i);
        }
    }

    public static void addArrowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        if (j > 0) {

            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        if (k > 0) {

            abstractarrowentity.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {

            abstractarrowentity.setFire(100);
        }
    }

}
