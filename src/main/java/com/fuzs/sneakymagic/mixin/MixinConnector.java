package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.SneakyMagic;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@SuppressWarnings("unused")
public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {

        Mixins.addConfiguration("META-INF/" + SneakyMagic.MODID + ".mixins.json");
    }

}
