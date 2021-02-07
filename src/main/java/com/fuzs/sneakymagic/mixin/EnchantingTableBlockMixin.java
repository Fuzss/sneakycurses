package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagicElements;
import com.fuzs.sneakymagic.element.EasyEnchantingElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mixin(EnchantingTableBlock.class)
public abstract class EnchantingTableBlockMixin extends ContainerBlock {

    protected EnchantingTableBlockMixin(Properties builder) {

        super(builder);
    }

    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    public void onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> callbackInfo) {

        EasyEnchantingElement element = SneakyMagicElements.getAs(SneakyMagicElements.EASY_ENCHANTING);
        if (!worldIn.isRemote && element.isEnabled() && element.itemsStay) {

            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof EnchantingTableTileEntity) {

                player.openContainer((INamedContainerProvider) tileentity);
            }

            callbackInfo.setReturnValue(ActionResultType.CONSUME);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {

        EasyEnchantingElement element = SneakyMagicElements.getAs(SneakyMagicElements.EASY_ENCHANTING);
        if (element.isEnabled() && element.itemsStay && !state.isIn(newState.getBlock())) {

            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof EnchantingTableTileEntity) {

                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

}
