package fuzs.sneakycurses.mixin.client;

import fuzs.sneakycurses.client.renderer.ModRenderType;
import fuzs.sneakycurses.client.util.GlintRenderStateHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiBufferSource.BufferSource.class)
abstract class BufferSourceMixin {

    @ModifyVariable(method = "getBuffer", at = @At("HEAD"), argsOnly = true)
    public RenderType getBuffer(RenderType renderType) {
        return GlintRenderStateHelper.getRenderState() ?
                ModRenderType.GLINT_RENDER_TYPES.inverse().getOrDefault(renderType, renderType) : renderType;
    }
}
