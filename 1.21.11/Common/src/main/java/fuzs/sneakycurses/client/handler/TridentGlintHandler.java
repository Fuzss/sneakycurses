package fuzs.sneakycurses.client.handler;

import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.renderer.entity.CustomItemRenderer;
import fuzs.sneakycurses.network.client.ServerboundRequestTridentItemMessage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;

import java.util.Optional;

public class TridentGlintHandler {
    public static final ContextKey<Optional<Boolean>> IS_TRIDENT_CURSED_KEY = new ContextKey<>(SneakyCurses.id(
            "is_trident_cursed"));

    public static EventResult onEntityLoad(Entity entity, ClientLevel clientLevel) {
        if (entity instanceof ThrownTrident) {
            // need to go this way around as during the server load event the entity has not yet been synced to clients
            MessageSender.broadcast(new ServerboundRequestTridentItemMessage(entity.getId()));
        }

        return EventResult.PASS;
    }

    public static void onExtractRenderState(Entity entity, EntityRenderState entityRenderState, float partialTick) {
        if (entity instanceof ThrownTrident thrownTrident && entityRenderState instanceof ThrownTridentRenderState) {
            // vanilla doesn't sync the stack to clients, we need to take care of that ourselves
            RenderStateExtraData.set(entityRenderState,
                    IS_TRIDENT_CURSED_KEY,
                    Optional.of(CustomItemRenderer.isItemStackCursed(thrownTrident.getPickupItemStackOrigin())));
        }
    }
}
