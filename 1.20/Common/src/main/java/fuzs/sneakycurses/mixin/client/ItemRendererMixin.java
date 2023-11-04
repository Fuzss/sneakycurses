package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import fuzs.sneakycurses.client.renderer.GlintRenderTypes;
import fuzs.sneakycurses.client.util.GlintColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
abstract class ItemRendererMixin {

    @Inject(method = "getFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void getFoilBuffer(MultiBufferSource buffer, RenderType renderType, boolean isItem, boolean glint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (glint && GlintColorHelper.shouldRenderCursedGlint()) {
            callback.setReturnValue(Minecraft.useShaderTransparency() && renderType == Sheets.translucentItemSheet() ? VertexMultiConsumer.create(buffer.getBuffer(GlintRenderTypes.cursedGlintTranslucent()), buffer.getBuffer(renderType)) : VertexMultiConsumer.create(buffer.getBuffer(isItem ? GlintRenderTypes.cursedGlint() : GlintRenderTypes.cursedEntityGlint()), buffer.getBuffer(renderType)));
        }
    }

    @Inject(method = "getArmorFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void getArmorFoilBuffer(MultiBufferSource buffer, RenderType renderType, boolean noEntity, boolean withGlint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (withGlint && GlintColorHelper.shouldRenderCursedGlint()) {
            callback.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(noEntity ? GlintRenderTypes.cursedArmorGlint() : GlintRenderTypes.cursedArmorEntityGlint())));
        }
    }

    @Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void getFoilBufferDirect(MultiBufferSource buffer, RenderType renderType, boolean noEntity, boolean withGlint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (withGlint && GlintColorHelper.shouldRenderCursedGlint())
            callback.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(noEntity ? GlintRenderTypes.cursedGlintDirect() : GlintRenderTypes.cursedEntityGlintDirect()), buffer.getBuffer(renderType)));
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render$0(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(itemStack);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render$1(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(ItemStack.EMPTY);
    }
}
