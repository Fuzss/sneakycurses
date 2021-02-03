package com.fuzs.sneakymagic.capability.container;

import net.minecraft.nbt.CompoundNBT;

public class ArrowCapability implements IArrowCapability {

    private int looting;

    @Override
    public void setLooting(int level) {

        this.looting = level;
    }

    @Override
    public int getLooting() {

        return this.looting;
    }

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(getName(), this.looting);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        this.looting = nbt.getInt(getName());
    }

    public static String getName() {

        return "ArrowEnchantments";
    }

}
