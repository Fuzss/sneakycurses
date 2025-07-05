package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sneakycurses.client.util.GlintItemStackRenderState;
import fuzs.sneakycurses.client.util.GlintRenderStateHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
abstract class ItemStackRenderStateMixin implements GlintItemStackRenderState {
    @Unique
    private boolean sneakycurses$glint;

    @Override
    public void sneakycurses$setGlint(boolean glint) {
        this.sneakycurses$glint = glint;
    }

    @Override
    public boolean sneakycurses$getGlint() {
        return this.sneakycurses$glint;
    }

    @Inject(method = "clear", at = @At("TAIL"))
    public void clear(CallbackInfo callback) {
        this.sneakycurses$setGlint(false);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render$0(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo callback) {
        GlintRenderStateHelper.copyRenderState(this);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render$1(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo callback) {
        GlintRenderStateHelper.clearRenderState();
    }
}
