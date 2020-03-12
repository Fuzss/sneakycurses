package com.fuzs.sneakymagic.common;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
                    applyCommonEnchantments(abstractarrowentity, stack);
                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    evt.getWorld().addEntity(abstractarrowentity);
                }

            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractArrowEntity) {

            AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) evt.getEntity();
            if (abstractarrowentity.getShooter() instanceof LivingEntity) {

                LivingEntity livingEntity = (LivingEntity) abstractarrowentity.getShooter();
                ItemStack stack = livingEntity.getActiveItemStack();

                if (stack.getItem() instanceof BowItem) {

                    // piercing enchantment for bows
                    this.applyPiercingEnchantment(abstractarrowentity, stack);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemUseTick(final LivingEntityUseItemEvent.Tick evt) {

        Item item = evt.getItem().getItem();
        int duration = evt.getItem().getUseDuration() - evt.getDuration();
        if (item instanceof BowItem && duration < 20 || item instanceof TridentItem && duration < 10) {

            // quick charge enchantment for bows and tridents
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, evt.getItem());
            evt.setDuration(evt.getDuration() - i);
        }
    }

    private void applyPiercingEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, stack);
        if (i > 0) {

            abstractarrowentity.func_213872_b((byte) i);
        }
    }

    private static void applyCommonEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

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

    public static void applyCrossbowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        applyCommonEnchantments(abstractarrowentity, stack);
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0) {

            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }
    }

}
