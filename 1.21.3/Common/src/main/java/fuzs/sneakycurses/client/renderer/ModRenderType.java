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
import net.minecraft.util.TriState;

public class ModRenderType extends RenderType {
    private static final ResourceLocation ENCHANTED_GLINT_ITEM = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ITEM.getPath());
    private static final ResourceLocation ENCHANTED_GLINT_ENTITY = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ENTITY.getPath());
    private static final RenderType ARMOR_ENTITY_GLINT = create("armor_entity_glint",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY,
                            TriState.DEFAULT,
                            false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false));
    private static final RenderType GLINT_TRANSLUCENT = create("glint_translucent",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false));
    private static final RenderType GLINT = create("glint",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));
    private static final RenderType ENTITY_GLINT = create("entity_glint",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY,
                            TriState.DEFAULT,
                            false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .createCompositeState(false));
    public static final BiMap<RenderType, RenderType> GLINT_RENDER_TYPES = ImmutableBiMap.of(armorEntityGlint(),
            RenderType.armorEntityGlint(),
            glintTranslucent(),
            RenderType.glintTranslucent(),
            glint(),
            RenderType.glint(),
            entityGlint(),
            RenderType.entityGlint());

    public static RenderType armorEntityGlint() {
        return ARMOR_ENTITY_GLINT;
    }

    public static RenderType glintTranslucent() {
        return GLINT_TRANSLUCENT;
    }

    public static RenderType glint() {
        return GLINT;
    }

    public static RenderType entityGlint() {
        return ENTITY_GLINT;
    }

    private ModRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType.CompositeRenderType create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, RenderType.CompositeState state) {
        return RenderType.create("cursed_" + name, format, mode, bufferSize, state);
    }
}
