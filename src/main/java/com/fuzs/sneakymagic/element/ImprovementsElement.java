package com.fuzs.sneakymagic.element;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.sneakymagic.SneakyMagicElements;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ImprovementsElement extends AbstractElement implements ICommonElement {
    
    private boolean trueInfinity;
    private boolean noProjectileResistance;
    public boolean requireSweepingEdge;
    private boolean boostImpaling;
    public boolean returnTridentFromVoid;
    public boolean noAxePenalty;
    
    @Override
    public String getDescription() {

        return "Apply handy buffs and improvements to a few enchantments.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onLivingHurt);
        this.addListener(this::onArrowNock);
        this.addListener(this::onRightClickItem);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true), v -> this.trueInfinity = v);
        addToConfig(builder.comment("Disables damage immunity when hit by a projectile. Makes it possible for entities to be hit by multiple projectiles at once.").define("No Projectile Immunity", true), v -> this.noProjectileResistance = v);
        addToConfig(builder.comment("Is the sweeping edge enchantment required to perform a sweep attack.").define("Require Sweeping Edge", false), v -> this.requireSweepingEdge = v);
        addToConfig(builder.comment("Makes the impaling enchantment apply to any creature in contact with rain or water.").define("Boost Impaling", true), v -> this.boostImpaling = v);
        addToConfig(builder.comment("Tridents enchanted with loyalty will return when thrown into the void.").define("Return Trident From Void", true), v -> this.returnTridentFromVoid = v);
        addToConfig(builder.comment("Prevent axes from receiving additional damage when attack an entity to make them work as proper weapons.").define("No Axe Penalty", true), v -> this.noAxePenalty = v);
    }

    private void onLivingHurt(final LivingHurtEvent evt) {

        // immediately reset damage immunity after being hit by any projectile, fixes multishot
        if (this.noProjectileResistance && evt.getSource().isProjectile()) {

            evt.getEntity().hurtResistantTime = 0;
        }
    }

    private void onArrowNock(final ArrowNockEvent evt) {

        // true infinity for bows
        if (this.trueInfinity && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, evt.getBow()) > 0) {

            evt.getPlayer().setActiveHand(evt.getHand());
            evt.setAction(ActionResult.resultConsume(evt.getBow()));
        }
    }

    private void onRightClickItem(final PlayerInteractEvent.RightClickItem evt) {

        // true infinity for crossbows
        ItemStack stack = evt.getPlayer().getHeldItem(evt.getHand());
        if (this.trueInfinity && stack.getItem() instanceof CrossbowItem && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0) {

            if (!CrossbowItem.isCharged(stack)) {

                // resetting isLoadingStart and isLoadingMiddle is not required as they're reset in CrossbowItem#func_219972_a anyways
                evt.getPlayer().setActiveHand(evt.getHand());
                evt.setCancellationResult(ActionResultType.CONSUME);
                evt.setCanceled(true);
            }
        }
    }

    public static float getModifierForCreature(ItemStack stack, CreatureAttribute creatureAttribute, LivingEntity livingentity) {

        ImprovementsElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_IMPROVEMENTS);
        if (element.isEnabled() && element.boostImpaling) {

            int impaleLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.IMPALING, stack);
            if (impaleLevel > 0 && livingentity.isInWaterRainOrBubbleColumn()) {

                creatureAttribute = CreatureAttribute.WATER;
            }
        }

        return EnchantmentHelper.getModifierForCreature(stack, creatureAttribute);
    }

}
