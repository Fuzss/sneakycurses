package fuzs.sneakycurses.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenEvents;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import fuzs.sneakycurses.client.renderer.GlintRenderTypes;
import fuzs.sneakycurses.network.client.ServerboundRequestTridentItemMessage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class SneakyCursesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ScreenEvents.afterInit(Screen.class).register(ItemTooltipHandler::onAfterInit);
        RenderLevelEvents.AFTER_TRANSLUCENT.register((levelRenderer, camera, gameRenderer, tickDelta, poseStack, projectionMatrix, frustum, level) -> {
            MultiBufferSource.BufferSource multiBufferSource = gameRenderer.getMinecraft().renderBuffers().bufferSource();
            GlintRenderTypes.GLINT_RENDER_TYPES.keySet().forEach(multiBufferSource::endBatch);
        });
        ClientEntityLevelEvents.LOAD.register((entity, level) -> {
            if (entity instanceof ThrownTrident) {
                // need to go this way around as during the server load event the entity has not yet been sycned to clients
                SneakyCurses.NETWORK.sendToServer(new ServerboundRequestTridentItemMessage(entity.getId()));
            }
            return EventResult.PASS;
        });
    }
}
