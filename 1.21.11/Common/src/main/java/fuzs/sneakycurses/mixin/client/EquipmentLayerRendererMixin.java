package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fuzs.sneakycurses.client.renderer.entity.CustomItemRenderer;
import fuzs.sneakycurses.client.renderer.rendertype.ModRenderTypes;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EquipmentLayerRenderer.class)
abstract class EquipmentLayerRendererMixin {

    @ModifyExpressionValue(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorEntityGlint()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    public RenderType renderLayers(RenderType renderType, @Local(argsOnly = true) ItemStack itemStack) {
        return CustomItemRenderer.isItemStackCursed(itemStack) ?
                ModRenderTypes.GLINT_RENDER_TYPES.getOrDefault(renderType, renderType) : renderType;
    }
}
