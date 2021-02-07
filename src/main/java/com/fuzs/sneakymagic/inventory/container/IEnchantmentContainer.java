package com.fuzs.sneakymagic.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;

public interface IEnchantmentContainer {

    void updateInventory(IInventory tileEntityInventory, PlayerInventory playerInventory);

}
