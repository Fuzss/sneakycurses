package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import fuzs.sneakycurses.client.renderer.entity.CustomItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelFeatureRenderer.class)
abstract class ModelFeatureRendererMixin {

    @ModifyVariable(method = "renderModel", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private VertexConsumer renderModel(VertexConsumer vertexConsumer, @Local(argsOnly = true) RenderType renderType) {
        return ModRenderType.GLINT_RENDER_TYPES.containsValue(renderType) ?
                CustomItemRenderer.getFoilBuffer(vertexConsumer) : vertexConsumer;
    }
}
