package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
abstract class LevelRendererMixin {

    @Inject(method = "lambda$addMainPass$1",
            at = @At(value = "CONSTANT", args = "stringValue=destroyProgress", shift = At.Shift.BEFORE))
    private void addMainPass(CallbackInfo callback, @Local(ordinal = 0) MultiBufferSource.BufferSource bufferSource) {
        ModRenderType.GLINT_RENDER_TYPES.values().forEach(bufferSource::endBatch);
    }
}
