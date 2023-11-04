package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sneakycurses.client.util.GlintColorHelper;
import fuzs.sneakycurses.mixin.client.accessor.ThrownTridentAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
abstract class ThrownTridentRendererMixin extends EntityRenderer<ThrownTrident> {

    protected ThrownTridentRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render$0(ThrownTrident entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo callback) {
        // vanilla doesn't sync the stack to clients, we need to take care of that ourselves
        GlintColorHelper.setTargetStack(((ThrownTridentAccessor) entity).sneakycurses$getTridentItem());
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render$1(ThrownTrident entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(ItemStack.EMPTY);
    }
}
