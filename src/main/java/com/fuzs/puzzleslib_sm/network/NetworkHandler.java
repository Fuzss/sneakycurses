package com.fuzs.puzzleslib_sm.network;

import com.fuzs.puzzleslib_sm.PuzzlesLib;
import com.fuzs.puzzleslib_sm.network.message.IMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * handler for network communications of all mods
 */
@SuppressWarnings("unused")
public class NetworkHandler {

    /**
     * protocol version for testing client-server compatibility of this mod
     */
    private final String PROTOCOL_VERSION = Integer.toString(1);
    /**
     * channel for sending messages
     */
    private final SimpleChannel MAIN_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(PuzzlesLib.MODID, "main_channel"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    /**
     * message index
     */
    private int discriminator;

    /**
     * register a message for a side
     * @param supplier supplier for message (called when receiving at executing end)
     * @param direction side message is to be executed at
     * @param <T> this message
     */
    public <T extends IMessage> void registerMessage(Supplier<T> supplier, NetworkDirection direction) {

        MAIN_CHANNEL.registerMessage(this.discriminator++, supplier.get().getClass(), IMessage::writePacketData, buf -> supplier.get().getPacketData(buf), (message, side) -> {

            NetworkEvent.Context ctx = side.get();
            if (ctx.getDirection() == direction) {

                PuzzlesLib.LOGGER.error("Receiving {} at wrong side!", message.getClass().getSimpleName());
            } else {

                ctx.enqueueWork(() -> message.processPacket(ctx.getSender()));
            }

            ctx.setPacketHandled(true);
        });
    }

    /**
     * send message from client to server
     * @param message message to send
     */
    public void sendToServer(IMessage message) {

        MAIN_CHANNEL.sendToServer(message);
    }

    /**
     * send message from server to client
     * @param message message to send
     * @param player client player to send to
     */
    public void sendTo(IMessage message, ServerPlayerEntity player) {

        MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    /**
     * send message from server to all clients
     * @param message message to send
     */
    public void sendToAll(IMessage message) {

        MAIN_CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    /**
     * send message from server to all clients in dimension
     * @param message message to send
     * @param dimension dimension to send message in
     */
    public void sendToDimension(IMessage message, RegistryKey<World> dimension) {

        MAIN_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
    }

}
