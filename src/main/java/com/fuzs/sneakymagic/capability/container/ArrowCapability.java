package com.fuzs.sneakymagic.capability.container;

import com.fuzs.puzzleslib_sm.util.INamespaceLocator;
import net.minecraft.nbt.CompoundNBT;

public class ArrowCapability implements IArrowCapability {

    private byte plundering;

    @Override
    public void setPlunderingLevel(byte level) {

        this.plundering = level;
    }

    @Override
    public byte getPlunderingLevel() {

        return this.plundering;
    }

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT nbt = new CompoundNBT();
        nbt.putByte(INamespaceLocator.format(getName()), this.plundering);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        this.plundering = nbt.getByte(INamespaceLocator.format(getName()));
    }

    public static String getName() {

        return "plundering";
    }

}
