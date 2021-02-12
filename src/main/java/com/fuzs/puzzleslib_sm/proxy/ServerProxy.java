package com.fuzs.puzzleslib_sm.proxy;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class ServerProxy implements IProxy {

    @Override
    public PlayerEntity getPlayer(@Nullable PlayerEntity player) {

        return player;
    }

}
