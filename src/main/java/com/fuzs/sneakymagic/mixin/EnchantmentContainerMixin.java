package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.common.SneakyMagicElements;
import com.fuzs.sneakymagic.common.element.EasyEnchantingElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mixin(EnchantmentContainer.class)
public abstract class EnchantmentContainerMixin extends Container {

    @Shadow
    @Final
    private IInventory tableInventory;
    @Shadow
    @Final
    private IntReferenceHolder xpSeed;

    @Unique
    private PlayerEntity player;
    @Unique
    private ItemStack lastStack;

    protected EnchantmentContainerMixin(@Nullable ContainerType<?> type, int id) {

        super(type, id);
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At("TAIL"))
    public void onInit(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable, CallbackInfo callbackInfo) {

        this.player = playerInventory.player;
    }

    @Inject(method = "onCraftMatrixChanged", at = @At(value = "TAIL"))
    private void onCraftMatrixChanged(IInventory inventoryIn, CallbackInfo callbackInfo) {

        EasyEnchantingElement element = SneakyMagicElements.getAs(SneakyMagicElements.EASY_ENCHANTING);
        if (element.isEnabled() && element.rerollEnchantments && inventoryIn == this.tableInventory) {

            ItemStack itemstack = inventoryIn.getStackInSlot(0);
            if (itemstack != this.lastStack) {

                this.lastStack = itemstack;
                if (itemstack.isEmpty() || !itemstack.isEnchantable()) {

                    this.player.onEnchant(ItemStack.EMPTY, 0);
                    this.xpSeed.set(this.player.getXPSeed());
                }
            }
        }
    }

    @Inject(method = "onContainerClosed", at = @At("HEAD"))
    public void onContainerClosed(PlayerEntity playerIn, CallbackInfo callbackInfo) {

        EasyEnchantingElement element = SneakyMagicElements.getAs(SneakyMagicElements.EASY_ENCHANTING);
        if (element.isEnabled() && element.rerollEnchantments) {

            this.player.onEnchant(ItemStack.EMPTY, 0);
            this.xpSeed.set(this.player.getXPSeed());
        }
    }

}
