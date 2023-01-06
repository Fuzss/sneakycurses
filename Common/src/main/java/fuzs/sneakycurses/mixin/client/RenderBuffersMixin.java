package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import fuzs.sneakycurses.client.core.ClientModServices;
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

    @Inject(method = "lambda$new$1", at = @At("TAIL"))
    private void lambda$new$1$Inject$Head(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, CallbackInfo callback) {
        put(map, ClientModServices.ABSTRACTIONS.cursedArmorGlint());
        put(map, ClientModServices.ABSTRACTIONS.cursedArmorEntityGlint());
        put(map, ClientModServices.ABSTRACTIONS.cursedGlint());
        put(map, ClientModServices.ABSTRACTIONS.cursedGlintDirect());
        put(map, ClientModServices.ABSTRACTIONS.cursedGlintTranslucent());
        put(map, ClientModServices.ABSTRACTIONS.cursedEntityGlint());
        put(map, ClientModServices.ABSTRACTIONS.cursedEntityGlintDirect());
    }

    @Shadow
    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> pMapBuilders, RenderType pRenderType) {
        throw new IllegalStateException();
    }
}
