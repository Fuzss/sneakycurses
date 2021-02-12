package com.fuzs.puzzleslib_sm.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientProxy implements IProxy {

    @Override
    public PlayerEntity getPlayer(PlayerEntity player) {

        return Minecraft.getInstance().player;
    }

}
