package com.fuzs.puzzleslib_sm.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

/**
 * network message template
 */
public interface IMessage {

    /**
     * writes message data to buffer
     * @param buf network data byte buffer
     */
    void writePacketData(final PacketBuffer buf);

    /**
     * reads message data from buffer
     * @param buf network data byte buffer
     */
    void readPacketData(final PacketBuffer buf);

    /**
     * call {@link #readPacketData} and return this
     * @param buf network data byte buffer
     * @param <T> this
     * @return instance of this
     */
    @SuppressWarnings("unchecked")
    default <T extends IMessage> T getPacketData(PacketBuffer buf) {

        this.readPacketData(buf);
        return (T) this;
    }

    /**
     * handles message on receiving side
     * @param player server player when sent from client
     */
    void processPacket(PlayerEntity player);

    /**
     * @return Minecraft client instance
     */
    static Minecraft getMinecraft() {

        return LogicalSidedProvider.INSTANCE.get(LogicalSide.CLIENT);
    }

}
