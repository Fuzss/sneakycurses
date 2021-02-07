package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import com.fuzs.sneakymagic.mixin.accessor.IAbstractArrowEntityAccessor;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

        ImprovementsElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_IMPROVEMENTS);
        if (element.isEnabled() && element.returnTridentFromVoid) {

            Entity entity = this.func_234616_v_();
            if (this.getPosY() < -64.0 && entity instanceof PlayerEntity && EnchantmentHelper.getLoyaltyModifier(this.thrownStack) > 0 && this.shouldReturnToThrower()) {

                this.setNoClip(true);
                this.onCollideWithPlayer((PlayerEntity) entity);
            }
        }
    }

    @Shadow
    abstract boolean shouldReturnToThrower();

    @Inject(method = "onEntityHit", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/projectile/TridentEntity;dealtDamage:Z", shift = At.Shift.AFTER))
    protected void onEntityHit(EntityRayTraceResult rayTraceResult, CallbackInfo callbackInfo) {

        Entity entity = rayTraceResult.getEntity();
        if (entity instanceof LivingEntity) {

            int knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, this.thrownStack);
            if (knockbackStrength > 0) {

                Vector3d vector3d = this.getMotion().mul(1.0, 0.0, 1.0).normalize().scale(knockbackStrength * 0.6);
                if (vector3d.lengthSquared() > 0.0) {

                    entity.addVelocity(vector3d.x, 0.1, vector3d.z);
                }
            }
        }

        int pierceLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, this.thrownStack);
        if (pierceLevel > 0) {

            IntOpenHashSet piercedEntities = ((IAbstractArrowEntityAccessor) this).getPiercedEntities();
            if (piercedEntities == null) {

                piercedEntities = new IntOpenHashSet(5);
                ((IAbstractArrowEntityAccessor) this).setPiercedEntities(piercedEntities);
            }

            piercedEntities.add(entity.getEntityId());
            if (piercedEntities.size() < pierceLevel) {

                this.dealtDamage = false;
            }
        }
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getModifierForCreature(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/CreatureAttribute;)F"))
    public float getModifierForCreature(ItemStack stack, CreatureAttribute creatureAttribute, EntityRayTraceResult rayTraceResult) {

        LivingEntity livingentity = (LivingEntity) rayTraceResult.getEntity();
        return ImprovementsElement.getModifierForCreature(stack, creatureAttribute, livingentity);
    }

}
