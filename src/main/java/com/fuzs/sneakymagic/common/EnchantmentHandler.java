package com.fuzs.sneakymagic.common;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class EnchantmentHandler {

    private static final EnchantmentType ACTUAL_WEAPON = EnchantmentType.create("ACTUAL_WEAPON", item -> item instanceof SwordItem || item instanceof AxeItem || item instanceof TridentItem);
    private static final EnchantmentType ARROW_LAUNCHER = EnchantmentType.create("ARROW_LAUNCHER", item -> item instanceof CrossbowItem || item instanceof BowItem);

    public EnchantmentHandler() {

        Lists.newArrayList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.IMPALING).forEach(ench -> ench.type = ACTUAL_WEAPON);
        Lists.newArrayList(Enchantments.FLAME, Enchantments.PUNCH, Enchantments.POWER, Enchantments.INFINITY, Enchantments.PIERCING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE).forEach(ench -> ench.type = ARROW_LAUNCHER);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onArrowNock(final ArrowNockEvent evt) {

        // true infinity for bows
        if (ConfigBuildHandler.TRUE_INFINITY.get() && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, evt.getBow()) > 0) {

            evt.getPlayer().setActiveHand(evt.getHand());
            evt.setAction(ActionResult.resultConsume(evt.getBow()));
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRightClickItem(final PlayerInteractEvent.RightClickItem evt) {

        // true infinity for crossbows
        ItemStack stack = evt.getPlayer().getHeldItem(evt.getHand());
        if (ConfigBuildHandler.TRUE_INFINITY.get() && stack.getItem() instanceof CrossbowItem && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0) {

            if (!CrossbowItem.isCharged(stack)) {

                // resetting isLoadingStart and isLoadingMiddle is not required as they're reset in CrossbowItem#func_219972_a anyways
                evt.getPlayer().setActiveHand(evt.getHand());
                evt.setCancellationResult(ActionResultType.CONSUME);
                evt.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPickupXp(final PlayerXpEvent.PickupXp evt) {

        if (!ConfigBuildHandler.SELECTIVE_MENDING.get()) {

            return;
        }

        ExperienceOrbEntity orb = evt.getOrb();
        PlayerEntity entityIn = evt.getPlayer();
        entityIn.xpCooldown = 2;
        entityIn.onItemPickup(orb, 1);
        Map.Entry<EquipmentSlotType, ItemStack> entry = this.getRandomItemWithEnchantment(entityIn);
        if (entry != null) {

            ItemStack itemstack = entry.getValue();
            if (!itemstack.isEmpty() && itemstack.isDamaged()) {

                int i = Math.min((int)(orb.xpValue * itemstack.getXpRepairRatio()), itemstack.getDamage());
                orb.xpValue -= i / 2;
                itemstack.setDamage(itemstack.getDamage() - i);
            }
        }

        if (orb.xpValue > 0) {

            entityIn.giveExperiencePoints(orb.xpValue);
        }

        orb.remove();
        evt.setCanceled(true);
    }

    private Map.Entry<EquipmentSlotType, ItemStack> getRandomItemWithEnchantment(LivingEntity entityIn) {

        Map<EquipmentSlotType, ItemStack> map = Enchantments.MENDING.getEntityEquipment(entityIn);
        if (map.isEmpty()) {

            return null;
        } else {

            List<Map.Entry<EquipmentSlotType, ItemStack>> list = Lists.newArrayList();

            for(Map.Entry<EquipmentSlotType, ItemStack> entry : map.entrySet()) {

                // modified to check for ItemStack#isDamaged
                ItemStack itemstack = entry.getValue();
                if (!itemstack.isEmpty() && itemstack.isDamaged() && EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, itemstack) > 0) {

                    list.add(entry);
                }
            }

            return list.isEmpty() ? null : list.get(entityIn.getRNG().nextInt(list.size()));
        }
    }

}