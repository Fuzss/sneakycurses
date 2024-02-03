package fuzs.sneakycurses.network;

import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.sneakycurses.mixin.client.accessor.AbstractArrowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

public record ClientboundTridentItemMessage(int entityId,
                                            ItemStack tridentItem) implements ClientboundMessage<ClientboundTridentItemMessage> {

    @Override
    public ClientMessageListener<ClientboundTridentItemMessage> getHandler() {
        return new ClientMessageListener<>() {

            @Override
            public void handle(ClientboundTridentItemMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                if (level.getEntity(message.entityId) instanceof ThrownTrident thrownTrident) {
                    ((AbstractArrowAccessor) thrownTrident).sneakycurses$setPickupItemStack(message.tridentItem);
                }
            }
        };
    }
}
