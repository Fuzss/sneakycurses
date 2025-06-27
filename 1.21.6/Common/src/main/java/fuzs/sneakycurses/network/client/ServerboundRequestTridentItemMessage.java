package fuzs.sneakycurses.network.client;

import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.sneakycurses.network.ClientboundTridentItemMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.projectile.ThrownTrident;

public record ServerboundRequestTridentItemMessage(int entityId) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundRequestTridentItemMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServerboundRequestTridentItemMessage::entityId,
            ServerboundRequestTridentItemMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.level()
                        .getEntity(ServerboundRequestTridentItemMessage.this.entityId) instanceof ThrownTrident thrownTrident) {
                    MessageSender.broadcast(PlayerSet.ofPlayer(context.player()),
                            new ClientboundTridentItemMessage(ServerboundRequestTridentItemMessage.this.entityId,
                                    thrownTrident.getPickupItemStackOrigin()));
                }
            }
        };
    }
}
