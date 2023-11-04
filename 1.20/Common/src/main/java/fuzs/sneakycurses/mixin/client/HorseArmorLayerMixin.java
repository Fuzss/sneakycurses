package fuzs.sneakycurses.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.sneakycurses.client.util.GlintColorHelper;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
abstract class HorseArmorLayerMixin extends RenderLayer<Horse, HorseModel<Horse>> {

    public HorseArmorLayerMixin(RenderLayerParent<Horse, HorseModel<Horse>> renderer) {
        super(renderer);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render$0(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, Horse livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(livingEntity.getArmor());
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render$1(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, Horse livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callback) {
        GlintColorHelper.setTargetStack(ItemStack.EMPTY);
    }
}
