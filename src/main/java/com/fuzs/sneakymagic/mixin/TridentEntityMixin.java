package com.fuzs.sneakymagic.mixin;

import com.fuzs.puzzleslib_sm.capability.CapabilityController;
import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import com.fuzs.sneakymagic.mixin.accessor.IAbstractArrowEntityAccessor;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@SuppressWarnings("unused")
@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends AbstractArrowEntity {

    @Shadow
    private ItemStack thrownStack;
    @Shadow
    private boolean dealtDamage;

    protected TridentEntityMixin(EntityType<? extends AbstractArrowEntity> type, World worldIn) {

        super(type, worldIn);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo callbackInfo) {

        ImprovementsElement element = (ImprovementsElement) SneakyMagic.ENCHANTMENT_IMPROVEMENTS;
        if (element.isEnabled() && element.returnTridentFromVoid) {

            Entity entity = this.getShooter();
            if (this.getPosY() < -64.0 && entity instanceof PlayerEntity && EnchantmentHelper.getLoyaltyModifier(this.thrownStack) > 0 && this.shouldReturnToThrower()) {

                this.setNoClip(true);
                this.onCollideWithPlayer((PlayerEntity) entity);
            }
        }
    }

    @Shadow
    abstract boolean shouldReturnToThrower();

    @Inject(method = "onEntityHit", at = @At("TAIL"))
    protected void onEntityHit(EntityRayTraceResult rayTraceResult, CallbackInfo callbackInfo) {
        
        Entity target = rayTraceResult.getEntity();
        if (target instanceof LivingEntity) {

            int knockbackStrength = ((IAbstractArrowEntityAccessor) this).getKnockbackStrength();
            if (knockbackStrength > 0) {

                // copied from punch behavior
                Vec3d vector3d = this.getMotion().mul(1.0, 0.0, 1.0).normalize().scale(knockbackStrength * 0.6);
                if (vector3d.lengthSquared() > 0.0) {

                    target.addVelocity(vector3d.x, 0.1, vector3d.z);
                }
            }
        }
        
        if (this.applyPiercing(target)) {

            this.dealtDamage = false;
        }
    }
    
    private boolean applyPiercing(Entity target) {

        int pierceLevel = this.getPierceLevel();
        if (pierceLevel > 0) {

            IntOpenHashSet piercedEntities = ((IAbstractArrowEntityAccessor) this).getPiercedEntities();
            if (piercedEntities == null) {

                piercedEntities = new IntOpenHashSet(5);
                ((IAbstractArrowEntityAccessor) this).setPiercedEntities(piercedEntities);
            }

            piercedEntities.add(target.getEntityId());
            if (piercedEntities.size() <= pierceLevel) {

                // reverting previous motion change
                this.setMotion(this.getMotion().mul(-100.0, -10.0, -100.0));

                return true;
            }
        }

        return false;
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getModifierForCreature(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/CreatureAttribute;)F"))
    public float getModifierForCreature(ItemStack stack, CreatureAttribute creatureAttribute, EntityRayTraceResult rayTraceResult) {

        // make impaling work on mobs when in contact with water or rain
        LivingEntity livingentity = (LivingEntity) rayTraceResult.getEntity();
        return ImprovementsElement.getModifierForCreature(stack, creatureAttribute, livingentity);
    }

    @Inject(method = "onCollideWithPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;onCollideWithPlayer(Lnet/minecraft/entity/player/PlayerEntity;)V"), cancellable = true)
    public void onCollideWithPlayer(PlayerEntity entityIn, CallbackInfo callbackInfo) {

        ImprovementsElement element = (ImprovementsElement) SneakyMagic.ENCHANTMENT_IMPROVEMENTS;
        if (element.isEnabled() && element.returnTridentToSlot) {

            returnTridentToSlot(entityIn);
            callbackInfo.cancel();
        }
    }

    private void returnTridentToSlot(PlayerEntity entityIn) {

        if (!this.world.isRemote && (this.inGround || this.getNoClip()) && this.arrowShake <= 0) {

            // getShooter
            boolean flag = this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED || this.pickupStatus == AbstractArrowEntity.PickupStatus.CREATIVE_ONLY && entityIn.abilities.isCreativeMode || this.getNoClip() && Objects.requireNonNull(this.getShooter()).getUniqueID() == entityIn.getUniqueID();
            if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {

                if (!CapabilityController.getCapability(this, ImprovementsElement.TRIDENT_SLOT_CAPABILITY).map(tridentSlot -> tridentSlot.addToInventory(entityIn, this.getArrowStack())).orElse(false)) {

                    flag = false;
                }
            }

            if (flag) {

                entityIn.onItemPickup(this, 1);
                this.remove();
            }
        }
    }

}
