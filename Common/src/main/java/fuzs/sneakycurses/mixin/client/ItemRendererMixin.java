package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import fuzs.sneakycurses.client.SneakyCursesClient;
import fuzs.sneakycurses.client.core.ClientModServices;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
abstract class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void setColorRuneTargetStack(ItemStack itemStackIn, ItemTransforms.TransformType transformTypeIn, boolean leftHand, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, BakedModel modelIn, CallbackInfo callbackInfo) {
        if (CurseMatcher.anyEnchantIsCursed(itemStackIn)) {
            SneakyCursesClient.targetStack = itemStackIn;
        } else {
            SneakyCursesClient.targetStack = ItemStack.EMPTY;
        }
    }

    @Inject(method = "getArmorFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void getArmorFoilBuffer$Inject$Head(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (pWithGlint && !SneakyCursesClient.targetStack.isEmpty()) {
            callback.setReturnValue(VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? ClientModServices.ABSTRACTIONS.cursedArmorGlint() : ClientModServices.ABSTRACTIONS.cursedArmorEntityGlint())));
        }
    }

    @Inject(method = "getFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void getFoilBuffer$Inject$Head(MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (pGlint && !SneakyCursesClient.targetStack.isEmpty()) {
        callback.setReturnValue(Minecraft.useShaderTransparency() && pRenderType == Sheets.translucentItemSheet() ? VertexMultiConsumer.create(pBuffer.getBuffer(ClientModServices.ABSTRACTIONS.cursedGlintTranslucent()), pBuffer.getBuffer(pRenderType)) : VertexMultiConsumer.create(pBuffer.getBuffer(pIsItem ? ClientModServices.ABSTRACTIONS.cursedGlint() : ClientModServices.ABSTRACTIONS.cursedEntityGlint()), pBuffer.getBuffer(pRenderType)));
        }
    }

    @Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void getFoilBufferDirect$Inject$Head(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint, CallbackInfoReturnable<VertexConsumer> callback) {
        if (pWithGlint && !SneakyCursesClient.targetStack.isEmpty())
        callback.setReturnValue(VertexMultiConsumer.create(pBuffer.getBuffer(pNoEntity ? ClientModServices.ABSTRACTIONS.cursedGlintDirect() : ClientModServices.ABSTRACTIONS.cursedEntityGlintDirect()), pBuffer.getBuffer(pRenderType)));
    }
}
