package fuzs.sneakycurses.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.RenderBuffersContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderPipelinesContext;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderLevelCallback;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import fuzs.sneakycurses.client.handler.TridentGlintHandler;
import fuzs.sneakycurses.client.packs.TransformingPackResources;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;

public class SneakyCursesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ScreenEvents.afterInit(Screen.class).register(ItemTooltipHandler::onAfterInit);
        RenderLevelCallback.TRANSLUCENT.register((LevelRenderer levelRenderer, Camera camera, GameRenderer gameRenderer, DeltaTracker deltaTracker, PoseStack poseStack, Frustum frustum, ClientLevel clientLevel) -> {
            MultiBufferSource.BufferSource multiBufferSource = gameRenderer.getMinecraft()
                    .renderBuffers()
                    .bufferSource();
            ModRenderType.GLINT_RENDER_TYPES.values().forEach(multiBufferSource::endBatch);
        });
        ClientEntityLevelEvents.LOAD.register(TridentGlintHandler::onEntityLoad);
        ExtractRenderStateCallback.EVENT.register(TridentGlintHandler::onExtractRenderState);
    }

    @Override
    public void onRegisterRenderBuffers(RenderBuffersContext context) {
        ModRenderType.GLINT_RENDER_TYPES.values().forEach(context::registerRenderBuffer);
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(SneakyCurses.id("grayscale_textures"),
                TransformingPackResources::new,
                true));
    }

    @Override
    public void onRegisterRenderPipelines(RenderPipelinesContext context) {
        context.registerRenderPipeline(ModRenderType.GLINT_RENDER_PIPELINE);
    }
}
