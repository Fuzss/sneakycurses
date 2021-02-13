package com.fuzs.puzzleslib_sm.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nonnull;

/**
 * message send from server to client
 */
public abstract class SMessage extends Message {

    @Override
    public final LogicalSide getExecutionSide() {

        return LogicalSide.CLIENT;
    }

    /**
     * separate class for executing message when received to work around sided limitations
     */
    protected abstract class SMessageProcessor extends Message.MessageProcessor<Minecraft> {

        @Nonnull
        @Override
        public final PlayerEntity getPlayer() {

            ClientPlayerEntity player = this.getInstance().player;
            assert player != null;
            return player;
        }

    }

}
