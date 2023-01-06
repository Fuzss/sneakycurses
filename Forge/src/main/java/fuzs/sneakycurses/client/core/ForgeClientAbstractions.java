package fuzs.sneakycurses.client.core;

import fuzs.sneakycurses.client.renderer.ModRenderType;
import net.minecraft.client.renderer.RenderType;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public RenderType cursedArmorGlint() {
        return ModRenderType.CURSED_ARMOR_GLINT;
    }

    @Override
    public RenderType cursedArmorEntityGlint() {
        return ModRenderType.CURSED_ARMOR_ENTITY_GLINT;
    }

    @Override
    public RenderType cursedGlintTranslucent() {
        return ModRenderType.CURSED_GLINT_TRANSLUCENT;
    }

    @Override
    public RenderType cursedGlint() {
        return ModRenderType.CURSED_GLINT;
    }

    @Override
    public RenderType cursedGlintDirect() {
        return ModRenderType.CURSED_GLINT_DIRECT;
    }

    @Override
    public RenderType cursedEntityGlint() {
        return ModRenderType.CURSED_ENTITY_GLINT;
    }

    @Override
    public RenderType cursedEntityGlintDirect() {
        return ModRenderType.CURSED_ENTITY_GLINT_DIRECT;
    }
}
