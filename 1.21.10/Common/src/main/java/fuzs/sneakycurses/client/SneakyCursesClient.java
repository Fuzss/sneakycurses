package fuzs.sneakycurses.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.RenderBuffersContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderPipelinesContext;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import fuzs.sneakycurses.client.handler.TridentGlintHandler;
import fuzs.sneakycurses.client.packs.TransformingPackResources;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import net.minecraft.client.gui.screens.Screen;

public class SneakyCursesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ScreenEvents.afterInit(Screen.class).register(ItemTooltipHandler::onAfterInit);
        ClientEntityLevelEvents.LOAD.register(TridentGlintHandler::onEntityLoad);
        ExtractRenderStateCallback.EVENT.register(TridentGlintHandler::onExtractRenderState);
    }

    @Override
    public void onRegisterRenderBuffers(RenderBuffersContext context) {
        ModRenderType.GLINT_RENDER_TYPES.values().forEach(context::registerRenderBuffer);
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.registerRepositorySource(PackResourcesHelper.buildClientPack(SneakyCurses.id("grayscale_glint"),
                TransformingPackResources::new,
                true));
    }

    @Override
    public void onRegisterRenderPipelines(RenderPipelinesContext context) {
        context.registerRenderPipeline(ModRenderType.GLINT_RENDER_PIPELINE);
    }
}
