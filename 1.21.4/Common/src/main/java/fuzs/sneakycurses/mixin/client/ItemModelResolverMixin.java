package fuzs.sneakycurses.mixin.client;

import fuzs.sneakycurses.client.util.GlintItemStackRenderState;
import fuzs.sneakycurses.client.util.GlintRenderStateHelper;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
abstract class ItemModelResolverMixin {

    @Inject(method = "updateForTopItem", at = @At("TAIL"))
    public void updateForTopItem(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, @Nullable Level level, @Nullable LivingEntity entity, int seed, CallbackInfo callback) {
        if (!stack.isEmpty()) {
            ((GlintItemStackRenderState) renderState).sneakycurses$setGlint(GlintRenderStateHelper.isItemStackCursed(
                    stack));
        }
    }
}
