package fuzs.sneakycurses.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.RenderBuffersContext;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderLevelEvents;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import fuzs.sneakycurses.network.client.ServerboundRequestTridentItemMessage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

public class SneakyCursesClient implements ClientModConstructor {
    public static final RenderPropertyKey<ItemStack> PICKUP_ITEM_STACK_RENDER_PROPERTY = new RenderPropertyKey<>(
            SneakyCurses.id("pickup_item_stack"));

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ScreenEvents.afterInit(Screen.class).register(ItemTooltipHandler::onAfterInit);
        RenderLevelEvents.AFTER_TRANSLUCENT.register((levelRenderer, camera, gameRenderer, tickDelta, poseStack, projectionMatrix, frustum, level) -> {
            MultiBufferSource.BufferSource multiBufferSource = gameRenderer.getMinecraft()
                    .renderBuffers()
                    .bufferSource();
            ModRenderType.GLINT_RENDER_TYPES.keySet().forEach(multiBufferSource::endBatch);
        });
        ClientEntityLevelEvents.LOAD.register((entity, level) -> {
            if (entity instanceof ThrownTrident) {
                // need to go this way around as during the server load event the entity has not yet been synced to clients
                SneakyCurses.NETWORK.sendMessage(new ServerboundRequestTridentItemMessage(entity.getId()));
            }
            return EventResult.PASS;
        });
        ExtractRenderStateCallback.EVENT.register((Entity entity, EntityRenderState entityRenderState, float partialTick) -> {
            if (entity instanceof ThrownTrident thrownTrident &&
                    entityRenderState instanceof ThrownTridentRenderState) {
                RenderPropertyKey.setRenderProperty(entityRenderState,
                        PICKUP_ITEM_STACK_RENDER_PROPERTY,
                        thrownTrident.getPickupItemStackOrigin());
            }
        });
    }

    @Override
    public void onRegisterRenderBuffers(RenderBuffersContext context) {
        ModRenderType.GLINT_RENDER_TYPES.keySet().forEach(context::registerRenderBuffer);
    }
}
