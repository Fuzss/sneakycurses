package fuzs.sneakycurses.client.core;

import net.minecraft.client.renderer.RenderType;

public interface ClientAbstractions {

    RenderType cursedArmorGlint();

    RenderType cursedArmorEntityGlint();

    RenderType cursedGlintTranslucent();

    RenderType cursedGlint();

    RenderType cursedGlintDirect();

    RenderType cursedEntityGlint();

    RenderType cursedEntityGlintDirect();
}
