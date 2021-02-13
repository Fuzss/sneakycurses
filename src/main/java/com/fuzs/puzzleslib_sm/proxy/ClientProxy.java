package com.fuzs.puzzleslib_sm.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * client proxy class
 */
public class ClientProxy implements IProxy<Minecraft> {

    @Override
    public Minecraft getInstance() {

        return LogicalSidedProvider.INSTANCE.get(LogicalSide.CLIENT);
    }

    @Nonnull
    @Override
    public PlayerEntity getPlayer(@Nullable PlayerEntity player) {

        return Objects.requireNonNull(this.getInstance().player);
    }

}
