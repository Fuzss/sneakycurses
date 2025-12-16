package fuzs.sneakycurses.network;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;

public record ClientboundTridentItemMessage(int entityId, ItemStack tridentItem) implements ClientboundPlayMessage {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTridentItemMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClientboundTridentItemMessage::entityId,
            ItemStack.OPTIONAL_STREAM_CODEC,
            ClientboundTridentItemMessage::tridentItem,
            ClientboundTridentItemMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.level()
                        .getEntity(ClientboundTridentItemMessage.this.entityId) instanceof ThrownTrident thrownTrident) {
                    thrownTrident.pickupItemStack = ClientboundTridentItemMessage.this.tridentItem;
                }
            }
        };
    }
}
