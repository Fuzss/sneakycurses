package com.fuzs.sneakymagic.mixin;

import com.fuzs.puzzleslib_sm.capability.CapabilityController;
import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.capability.container.TridentSlot;
import com.fuzs.sneakymagic.element.CompatibilityElement;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;
import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item {

    public TridentItemMixin(Properties properties) {

        super(properties);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {

        ImprovementsElement element = (ImprovementsElement) SneakyMagic.ENCHANTMENT_IMPROVEMENTS;
        if (element.isEnabled() && element.repairTridentWithPrismarine) {

            return repair.getItem().isIn(Tags.Items.DUSTS_PRISMARINE);
        }

        return false;
    }

    @Redirect(method = "onPlayerStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"))
    public boolean addEntity(World worldToAddIn, Entity tridentEntity, ItemStack stack, World worldIn2, LivingEntity itemUserEntity) {

        ImprovementsElement element = (ImprovementsElement) SneakyMagic.ENCHANTMENT_IMPROVEMENTS;
        if (element.isEnabled() && element.returnTridentToSlot && itemUserEntity instanceof PlayerEntity && itemUserEntity.getActiveItemStack().isItemEqual(stack)) {

            Optional<TridentSlot> tridentSlot = CapabilityController.getCapability(tridentEntity, ImprovementsElement.TRIDENT_SLOT_CAPABILITY).resolve();
            if (tridentSlot.isPresent()) {

                if (itemUserEntity.getActiveHand() == Hand.OFF_HAND) {

                    tridentSlot.get().setSlot(-2);
                } else {

                    tridentSlot.get().setSlot(((PlayerEntity) itemUserEntity).inventory.currentItem);
                }
            }
        }

        // add bow enchantments
        CompatibilityElement.applyPiercingEnchantment((AbstractArrowEntity) tridentEntity, stack);
        int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
        if (knockbackLevel > 0) {

            ((AbstractArrowEntity) tridentEntity).setKnockbackStrength(knockbackLevel);
        }

        return worldToAddIn.addEntity(tridentEntity);
    }

}
