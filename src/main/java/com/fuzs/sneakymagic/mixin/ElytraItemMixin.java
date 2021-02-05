package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.common.SneakyMagicElements;
import com.fuzs.sneakymagic.common.element.AnvilTweaksElement;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(ElytraItem.class)
public abstract class ElytraItemMixin extends Item {

    public ElytraItemMixin(Properties properties) {

        super(properties);
    }

    @Inject(method = "getIsRepairable", at = @At("HEAD"))
    public void getIsRepairable(ItemStack toRepair, ItemStack repair, CallbackInfoReturnable<Boolean> callbackInfo) {

        AnvilTweaksElement element = SneakyMagicElements.getAs(SneakyMagicElements.ANVIL_TWEAKS);
        if (element.isEnabled() && element.repairElytraWithLeather) {

            if (repair.getItem().isIn(Tags.Items.LEATHER)) {

                callbackInfo.setReturnValue(true);
            }
        }
    }

}
