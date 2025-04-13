package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import fuzs.sneakycurses.client.SneakyCursesClient;
import fuzs.sneakycurses.client.util.GlintRenderStateHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
abstract class ThrownTridentRendererMixin extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {

    protected ThrownTridentRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render$0(ThrownTridentRenderState thrownTridentRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CallbackInfo callback) {
        // vanilla doesn't sync the stack to clients, we need to take care of that ourselves
        ItemStack itemStack = RenderPropertyKey.getRenderProperty(thrownTridentRenderState,
                SneakyCursesClient.PICKUP_ITEM_STACK_RENDER_PROPERTY);
        GlintRenderStateHelper.extractRenderState(itemStack);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render$1(ThrownTridentRenderState thrownTridentRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CallbackInfo callback) {
        GlintRenderStateHelper.clearRenderState();
    }
}
