package fuzs.sneakycurses.client.handler;

import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.network.client.ServerboundRequestTridentItemMessage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

public class TridentGlintHandler {
    public static final RenderPropertyKey<ItemStack> PICKUP_ITEM_STACK_RENDER_PROPERTY = new RenderPropertyKey<>(
            SneakyCurses.id("pickup_item_stack"));

    public static EventResult onEntityLoad(Entity entity, ClientLevel level) {
        if (entity instanceof ThrownTrident) {
            // need to go this way around as during the server load event the entity has not yet been synced to clients
            MessageSender.broadcast(new ServerboundRequestTridentItemMessage(entity.getId()));
        }
        return EventResult.PASS;
    }

    public static void onExtractRenderState(Entity entity, EntityRenderState entityRenderState, float partialTick) {
        if (entity instanceof ThrownTrident thrownTrident && entityRenderState instanceof ThrownTridentRenderState) {
            RenderPropertyKey.set(entityRenderState,
                    PICKUP_ITEM_STACK_RENDER_PROPERTY,
                    thrownTrident.getPickupItemStackOrigin());
        }
    }
}
