package fuzs.sneakycurses.client.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class GlintRenderTypes extends RenderType {
    private static final ResourceLocation ENCHANTED_GLINT_ITEM = SneakyCurses.id(
            ItemRenderer.ENCHANTED_GLINT_ITEM.getPath());
    private static final ResourceLocation ENCHANTED_GLINT_ENTITY = SneakyCurses.id(
            ItemRenderer.ENCHANTED_GLINT_ENTITY.getPath());
    private static final RenderType ARMOR_ENTITY_GLINT = create("armor_entity_glint", DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS, 1536, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false)
    );
    private static final RenderType GLINT_TRANSLUCENT = create("glint_translucent", DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS, 1536, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false)
    );
    private static final RenderType GLINT = create("glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS,
            1536, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER).setTextureState(
                    new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(
                    COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(
                    GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).createCompositeState(false)
    );
    private static final RenderType ENTITY_GLINT = create("entity_glint", DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS, 1536, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .createCompositeState(false)
    );
    private static final RenderType ENTITY_GLINT_DIRECT = create("entity_glint_direct",
            DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 1536,
            RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(
                    new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(
                    COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(
                    GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false)
    );
    public static final BiMap<RenderType, RenderType> GLINT_RENDER_TYPES = ImmutableBiMap.of(ARMOR_ENTITY_GLINT,
            armorEntityGlint(), GLINT_TRANSLUCENT, glintTranslucent(), GLINT, glint(), ENTITY_GLINT, entityGlint(),
            ENTITY_GLINT_DIRECT, entityGlintDirect()
    );

    private GlintRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType.CompositeRenderType create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, RenderType.CompositeState state) {
        return RenderType.create("cursed_" + name, format, mode, bufferSize, state);
    }
}
