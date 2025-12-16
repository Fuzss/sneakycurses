package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.sneakycurses.client.handler.TridentGlintHandler;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Optional;

@Mixin(ThrownTridentRenderer.class)
abstract class ThrownTridentRendererMixin extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {

    protected ThrownTridentRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyVariable(method = "submit", at = @At("STORE"))
    public List<RenderType> submit(List<RenderType> list, @Local(argsOnly = true) ThrownTridentRenderState renderState) {
        boolean isCursed = RenderStateExtraData.getOrDefault(renderState,
                TridentGlintHandler.IS_TRIDENT_CURSED_KEY,
                Optional.empty()).orElse(false);
        return isCursed ? list.stream().map((RenderType renderType) -> {
            return ModRenderType.GLINT_RENDER_TYPES.getOrDefault(renderType, renderType);
        }).toList() : list;
    }
}
