package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sneakycurses.client.util.GlintColorHelper;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentLayerRenderer.class)
abstract class EquipmentLayerRendererMixin {

    @Inject(
            method = "renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At("HEAD")
    )
    public void renderLayers$0(EquipmentModel.LayerType layerType, ResourceLocation equipmentModel, Model armorModel, ItemStack item, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, @Nullable ResourceLocation playerTexture, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(item);
    }

    @Inject(
            method = "renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At("TAIL")
    )
    public void renderLayers$1(EquipmentModel.LayerType layerType, ResourceLocation equipmentModel, Model armorModel, ItemStack item, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, @Nullable ResourceLocation playerTexture, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(ItemStack.EMPTY);
    }
}
