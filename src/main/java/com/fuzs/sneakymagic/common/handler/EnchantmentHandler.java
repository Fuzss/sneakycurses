package com.fuzs.sneakymagic.common.handler;

import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantmentHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent evt) {

        // immediately reset damage immunity after being hit by any projectile, fixes multishot
        if (evt.getSource().isProjectile() && ConfigBuildHandler.NO_PROJECTILE_RESISTANCE.get()) {

            evt.getEntity().hurtResistantTime = 0;
        }
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

}