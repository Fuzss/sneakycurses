package com.fuzs.sneakymagic.capability.container;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IArrowPlundering extends INBTSerializable<CompoundNBT> {

    void setPlunderingLevel(byte level);

    byte getPlunderingLevel();

}
