package fuzs.sneakycurses.network.client;

import fuzs.puzzleslib.api.network.v3.PlayerSet;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.network.ClientboundTridentItemMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.projectile.ThrownTrident;

public record ServerboundRequestTridentItemMessage(int entityId) implements ServerboundMessage<ServerboundRequestTridentItemMessage> {

    @Override
    public ServerMessageListener<ServerboundRequestTridentItemMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundRequestTridentItemMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                if (level.getEntity(message.entityId) instanceof ThrownTrident thrownTrident) {
                    SneakyCurses.NETWORK.sendMessage(PlayerSet.ofPlayer(player),
                            new ClientboundTridentItemMessage(message.entityId,
                                    thrownTrident.getPickupItemStackOrigin()));
                }
            }
        };
    }
}
