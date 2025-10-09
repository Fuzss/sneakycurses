package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.sneakycurses.client.renderer.entity.CustomItemRenderer;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({BlockModelWrapper.class, SpecialModelWrapper.class})
abstract class ModelWrapperMixin {

    @ModifyVariable(method = "update", at = @At("STORE"))
    public ItemStackRenderState.FoilType update(ItemStackRenderState.FoilType foilType, @Local(argsOnly = true) ItemStack itemStack) {
        if (CustomItemRenderer.isItemStackCursed(itemStack)) {
            return foilType == ItemStackRenderState.FoilType.SPECIAL ? CustomItemRenderer.SPECIAL_CURSE_FOIL_TYPE :
                    CustomItemRenderer.STANDARD_CURSE_FOIL_TYPE;
        } else {
            return foilType;
        }
    }
}
