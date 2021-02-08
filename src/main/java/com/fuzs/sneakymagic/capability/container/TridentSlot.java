package com.fuzs.sneakymagic.capability.container;

import com.fuzs.puzzleslib_sm.util.INamespaceLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class TridentSlot implements ITridentSlot {

    private int slot = -1;

    @Override
    public void setSlot(int slot) {

        this.slot = slot;
    }

    @Override
    public int getSlot() {

        return this.slot;
    }

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(INamespaceLocator.format(getName()), this.slot);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        this.slot = nbt.getInt(INamespaceLocator.format(getName()));
    }

    public boolean addToInventory(PlayerEntity player, ItemStack stack) {

        int slot = getSlotInInventory(player.inventory, this.getSlot());
        if (isOffhand(slot)) {

            player.setItemStackToSlot(EquipmentSlotType.OFFHAND, stack);
            // just some stuff PlayerInventory::add also does (stack wasn't copied so it's still the same)
            stack.setAnimationsToGo(5);

            return true;
        } else {

            if (player.inventory.add(slot, stack)) {

                // work around for protected LivingEntity::playEquipSound method, the stack is empty so nothing will be added
                player.addItemStackToInventory(stack);

                return true;
            }
        }

        return false;
    }

    private static int getSlotInInventory(PlayerInventory inventory, int slot) {

        if (slot != -1) {

            // try to return to main hand as secondary option
            int currentSlot = inventory.getCurrentItem().isEmpty() ? inventory.currentItem : -1;
            if (isSlotAvailable(inventory, slot)) {

                currentSlot = slot;
            }

            return currentSlot;
        }

        return -1;
    }

    private static boolean isSlotAvailable(PlayerInventory inventory, int slot) {

        return isOffhand(slot) ? inventory.offHandInventory.get(0).isEmpty() : inventory.getStackInSlot(slot).isEmpty();
    }

    private static boolean isOffhand(int slot) {

        // 40 is actual slot, but maybe a mod adds more inventory slots somehow, and -1 is already used elsewhere
        return slot == -2;
    }

    public static String getName() {

        return "trident_slot";
    }

}
