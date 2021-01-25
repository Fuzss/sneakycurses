package com.fuzs.sneakymagic.common.element;

import com.fuzs.puzzleslib_sm.config.deserialize.EntryCollectionBuilder;
import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class CompatibilityElement extends AbstractElement implements ICommonElement {

    @Override
    public String getDescription() {

        return "Apply handy buffs and improvements to a few enchantments.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onArrowLoose);
        this.addListener(this::onEntityJoinWorld);
        this.addListener(this::onItemUseTick);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String[] getCommonDescription() {

        return new String[]{"Only enchantments included by default are guaranteed to work. While any modded enchantments or other vanilla enchantments can work, they are highly unlikely to do so.",
                "The blacklists for each item group are supposed to disable items which can be enchanted, but where the enchantments do not function as expected.",
                EntryCollectionBuilder.CONFIG_STRING};
    }

    private void onArrowLoose(final ArrowLooseEvent evt) {

        // multishot enchantment for bows
        ItemStack stack = evt.getBow();
        if (evt.hasAmmo() && EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) {

            float velocity = BowItem.getArrowVelocity(evt.getCharge());
            if ((double) velocity >= 0.1) {

                PlayerEntity playerentity = evt.getPlayer();
                ItemStack itemstack = playerentity.findAmmo(stack);
                ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

                for (int i = 0; i < 2; i++) {

                    AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(evt.getWorld(), itemstack, playerentity);
                    // shoot
                    abstractarrowentity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, velocity * 3.0F, 10.0F);
                    applyCommonEnchantments(abstractarrowentity, stack);
                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    evt.getWorld().addEntity(abstractarrowentity);
                }
            }
        }
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractArrowEntity) {

            AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) evt.getEntity();
            if (abstractarrowentity.func_234616_v_() instanceof LivingEntity) {

                // getShooter
                LivingEntity livingEntity = (LivingEntity) abstractarrowentity.func_234616_v_();
                assert livingEntity != null;
                ItemStack stack = livingEntity.getActiveItemStack();
                if (stack.getItem() instanceof BowItem) {

                    // piercing enchantment for bows
                    applyPiercingEnchantment(abstractarrowentity, stack);
                }
            }
        }
    }

    private void onItemUseTick(final LivingEntityUseItemEvent.Tick evt) {

        Item item = evt.getItem().getItem();
        int duration = evt.getItem().getUseDuration() - evt.getDuration();
        if (item instanceof BowItem && duration < 20 || item instanceof TridentItem && duration < 10) {

            // quick charge enchantment for bows and tridents
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, evt.getItem());
            evt.setDuration(evt.getDuration() - i);
        }
    }

    private static void applyPiercingEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, stack);
        if (i > 0) {

            abstractarrowentity.setPierceLevel((byte) i);
        }
    }

    public static void applyCommonEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        if (j > 0) {

            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5 + 0.5);
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
