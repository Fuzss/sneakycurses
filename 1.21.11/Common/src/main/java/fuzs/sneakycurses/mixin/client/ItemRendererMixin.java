package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import fuzs.sneakycurses.client.renderer.entity.CustomItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ItemRenderer.class)
abstract class ItemRendererMixin {

    @ModifyVariable(method = "renderItem", at = @At("LOAD"))
    private static VertexConsumer renderItem(VertexConsumer vertexConsumer, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
        if (CustomItemRenderer.isCurseFoilType(foilType)) {
            if (foilType == CustomItemRenderer.SPECIAL_CURSE_FOIL_TYPE) {
                PoseStack.Pose pose = poseStack.last().copy();
                if (displayContext == ItemDisplayContext.GUI) {
                    MatrixUtil.mulComponentWise(pose.pose(), 0.5F);
                } else if (displayContext.firstPerson()) {
                    MatrixUtil.mulComponentWise(pose.pose(), 0.75F);
                }

                return CustomItemRenderer.getSpecialFoilBuffer(bufferSource, renderType, pose);
            } else {
                return CustomItemRenderer.getFoilBuffer(bufferSource, renderType, true, true);
            }
        } else {
            return vertexConsumer;
        }
    }
}
