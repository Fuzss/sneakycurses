package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import java.util.Set;

@SuppressWarnings("unused")
@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends ToolItem {

    public AxeItemMixin(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builderIn) {

        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {

        ImprovementsElement element = SneakyMagicElements.getAs(SneakyMagicElements.ENCHANTMENT_IMPROVEMENTS);
        if (element.isEnabled() && element.noAxePenalty) {

            stack.damageItem(1, attacker, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            return true;
        }

        return super.hitEntity(stack, target, attacker);
    }

}
