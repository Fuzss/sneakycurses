package com.fuzs.puzzleslib_sm.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

public interface IProxy {

    static IProxy getProxy() {

        return DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    PlayerEntity getPlayer(@Nullable PlayerEntity player);

}
