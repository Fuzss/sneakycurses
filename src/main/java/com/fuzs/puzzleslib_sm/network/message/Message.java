package com.fuzs.puzzleslib_sm.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import javax.annotation.Nonnull;

/**
 * network message template
 */
public abstract class Message {

    /**
     * writes message data to buffer
     * @param buf network data byte buffer
     */
    public abstract void writePacketData(final PacketBuffer buf);

    /**
     * reads message data from buffer
     * @param buf network data byte buffer
     */
    protected abstract void readPacketData(final PacketBuffer buf);

    /**
     * call {@link #readPacketData} and return this
     * @param buf network data byte buffer
     * @param <T> this
     * @return instance of this
     */
    @SuppressWarnings("unchecked")
    public final <T extends Message> T getPacketData(PacketBuffer buf) {

        this.readPacketData(buf);
        return (T) this;
    }

    /**
     * handles message on receiving side
     */
    public final void processPacket() {

        this.getProcessor().process();
    }

    /**
     * @return message processor to run when received
     */
    protected abstract MessageProcessor<?> getProcessor();

    /**
     * side message is executed at
     * @return {@link LogicalSide#CLIENT} or {@link LogicalSide#SERVER}
     */
    public abstract LogicalSide getExecutionSide();

    /**
     * separate class for executing message when received to work around sided limitations
     * @param <T> Minecraft client or server instance type on receiving end
     */
    abstract class MessageProcessor<T extends RecursiveEventLoop<?>> {

        /**
         * action to perform when this message is received
         */
        protected abstract void process();

        /**
         * @return Minecraft client or server instance
         */
        public final T getInstance() {

            return LogicalSidedProvider.INSTANCE.get(Message.this.getExecutionSide());
        }

        /**
         * @return player entity depending on side
         */
        @Nonnull
        public abstract PlayerEntity getPlayer();

        /**
         * @return world object depending on side
         */
        public final World getWorld() {

            return this.getPlayer().world;
        }

    }

}
