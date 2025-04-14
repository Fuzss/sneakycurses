package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import fuzs.sneakycurses.client.util.GlintRenderStateHelper;
import fuzs.sneakycurses.config.ClientConfig;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
abstract class ItemRendererMixin {

    @ModifyArg(
            method = {"getFoilBuffer", "getArmorFoilBuffer"}, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
    )
    )
    private static RenderType getFoilBuffer(RenderType renderType) {
        return GlintRenderStateHelper.getRenderState() ?
                ModRenderType.GLINT_RENDER_TYPES.getOrDefault(renderType, renderType) : renderType;
    }

    @ModifyArg(
            method = {"getFoilBuffer", "getArmorFoilBuffer"}, at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexMultiConsumer;create(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lcom/mojang/blaze3d/vertex/VertexConsumer;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
    ), index = 0
    )
    private static VertexConsumer getFoilBuffer(VertexConsumer vertexConsumer) {
        return new OutlineBufferSource.EntityOutlineGenerator(vertexConsumer,
                ARGB.opaque(SneakyCurses.CONFIG.get(ClientConfig.class).cursedGlintColor.getTextureDiffuseColor()));
    }
}
