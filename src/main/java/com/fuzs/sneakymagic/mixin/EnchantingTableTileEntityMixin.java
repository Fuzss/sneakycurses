package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.inventory.container.IEnchantmentContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mixin(EnchantingTableTileEntity.class)
public abstract class EnchantingTableTileEntityMixin extends TileEntity implements IInventory, INamedContainerProvider, ISidedInventory, INameable {

    private final LazyOptional<? extends IItemHandler>[] handlers = createHandlers(this);
    private NonNullList<ItemStack> enchantingItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
    private LockCode code = LockCode.EMPTY_CODE;
    private EnchantmentContainer container;

    public EnchantingTableTileEntityMixin(TileEntityType<?> tileEntityTypeIn) {

        super(tileEntityTypeIn);
    }

    @Inject(method = "write", at = @At("TAIL"))
    public void write(CompoundNBT compound, CallbackInfoReturnable<CompoundNBT> callbackInfo) {

        ItemStackHelper.saveAllItems(compound, this.enchantingItemStacks);
        this.code.write(compound);
    }

    @Inject(method = "read", at = @At("TAIL"))
    public void read(BlockState state, CompoundNBT nbt, CallbackInfo callbackInfo) {

        this.enchantingItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.enchantingItemStacks);
        this.code = LockCode.read(nbt);
    }

    @Override
    public void markDirty() {

        super.markDirty();
        if (this.container != null) {

            this.container.onCraftMatrixChanged(this);
        }
    }

    @Override
    public int getSizeInventory() {

        return this.enchantingItemStacks.size();
    }

    @Override
    public boolean isEmpty() {

        for (ItemStack itemstack : this.enchantingItemStacks) {

            if (!itemstack.isEmpty()) {

                return false;
            }
        }

        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {

        return index >= 0 && index < this.enchantingItemStacks.size() ? this.enchantingItemStacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {

        return ItemStackHelper.getAndSplit(this.enchantingItemStacks, index, count);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {

        return ItemStackHelper.getAndRemove(this.enchantingItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {

        if (index >= 0 && index < this.enchantingItemStacks.size()) {

            this.enchantingItemStacks.set(index, stack);
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {

        assert this.world != null;
        if (this.world.getTileEntity(this.pos) != this) {

            return false;
        } else {

            return !(player.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) > 64.0);
        }
    }

    @Override
    public void clear() {

        this.enchantingItemStacks.clear();
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {

        if (index == 1) {

            return Tags.Items.GEMS_LAPIS.contains(stack.getItem());
        } else if (index == 0) {

            return (stack.isEnchantable() || stack.getItem() == Items.BOOK) && this.enchantingItemStacks.get(index).isEmpty();
        }

        return false;
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {

        return side == Direction.UP || side == Direction.DOWN ? new int[]{0} : new int[]{1};
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction) {

        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {

        // only allow extracting of enchantable item
        return index == 0 && (stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {

        return this.getName();
    }

    public boolean canOpen(PlayerEntity p_213904_1_) {

        return LockableTileEntity.canUnlock(p_213904_1_, this.code, this.getDisplayName());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, @Nonnull PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {

        return this.canOpen(p_createMenu_3_) ? this.createMenu(p_createMenu_1_, p_createMenu_2_) : null;
    }

    @SuppressWarnings("ConstantConditions")
    protected Container createMenu(int id, PlayerInventory player) {

        this.container = new EnchantmentContainer(id, player, IWorldPosCallable.of(this.world, this.pos));
        ((IEnchantmentContainer) this.container).updateInventory(this, player);
        return this.container;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {

        if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

            if (facing == Direction.UP) {

                return this.handlers[0].cast();
            } else if (facing == Direction.DOWN) {

                return this.handlers[1].cast();
            } else {

                return this.handlers[2].cast();
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    protected void invalidateCaps() {

        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {

            handler.invalidate();
        }
    }

    private static LazyOptional<? extends IItemHandler>[] createHandlers(ISidedInventory inv) {

        // working around a mixin limitation
        return SidedInvWrapper.create(inv, Direction.UP, Direction.DOWN, Direction.NORTH);
    }

}
