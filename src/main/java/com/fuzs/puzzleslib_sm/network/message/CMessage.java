package com.fuzs.puzzleslib_sm.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nonnull;

/**
 * message send from server to client
 */
public abstract class CMessage extends Message {

    /**
     * server player for message source client
     */
    private ServerPlayerEntity player;

    /**
     * set server player
     * @param player player from message context
     */
    public final void setPlayer(ServerPlayerEntity player) {

        this.player = player;
    }

    @Override
    public final LogicalSide getExecutionSide() {

        return LogicalSide.SERVER;
    }

    /**
     * separate class for executing message when received to work around sided limitations
     */
    protected abstract class CMessageProcessor extends Message.MessageProcessor<MinecraftServer> {

        @Nonnull
        @Override
        public final PlayerEntity getPlayer() {

            assert CMessage.this.player != null;
            return CMessage.this.player;
        }

    }

}
