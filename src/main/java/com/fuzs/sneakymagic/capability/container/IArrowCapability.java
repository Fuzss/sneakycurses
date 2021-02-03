package com.fuzs.sneakymagic.capability.container;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IArrowCapability extends INBTSerializable<CompoundNBT> {

    void setLooting(int level);

    int getLooting();

}
