package com.fuzs.sneakymagic.capability.container;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITridentSlot extends INBTSerializable<CompoundNBT> {

    void setSlot(int slot);

    int getSlot();

}
