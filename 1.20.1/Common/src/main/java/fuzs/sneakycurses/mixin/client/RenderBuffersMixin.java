package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import fuzs.sneakycurses.client.renderer.GlintRenderTypes;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
abstract class RenderBuffersMixin {

    @SuppressWarnings("target")
    @Inject(method = "lambda$new$1(Lit/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap;)V", at = @At("TAIL"))
    private void putAll(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, CallbackInfo callback) {
        GlintRenderTypes.GLINT_RENDER_TYPES.keySet().forEach(renderType -> {
            put(map, renderType);
        });
    }

    @Shadow
    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> mapBuilders, RenderType renderType) {
        throw new RuntimeException();
    }
}
