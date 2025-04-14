package fuzs.sneakycurses.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.packs.TransformingPackResources;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.TriState;

import java.util.Map;

/**
 * Our render pipeline uses a custom glint shader that respects the color parameters passed to a vertex consumer.
 * <p>
 * The render types use a dynamically generated copy of the glint textures that is converted to grayscale and brightened
 * to support dying via a custom vertex consumer implementation at
 * {@link net.minecraft.client.renderer.OutlineBufferSource.EntityOutlineGenerator}.
 */
public abstract class ModRenderType extends RenderType {
    /**
     * @see RenderPipelines#GLINT
     */
    public static final RenderPipeline GLINT_RENDER_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_SNIPPET,
                    RenderPipelines.FOG_NO_COLOR_SNIPPET)
            .withLocation(SneakyCurses.id("pipeline/glint"))
            .withVertexShader(SneakyCurses.id("core/glint"))
            .withFragmentShader(SneakyCurses.id("core/glint"))
            .withSampler("Sampler0")
            .withUniform("TextureMat", UniformType.MATRIX4X4)
            .withUniform("GlintAlpha", UniformType.FLOAT)
            .withDepthWrite(false)
            .withCull(false)
            .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
            .withBlend(BlendFunction.GLINT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();
    /**
     * @see RenderType#ARMOR_ENTITY_GLINT
     */
    private static final RenderType ARMOR_ENTITY_GLINT = create(SneakyCurses.id("armor_entity_glint").toString(),
            1536,
            GLINT_RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(TransformingPackResources.ENCHANTED_GLINT_ARMOR,
                            TriState.DEFAULT,
                            false))
                    .setTexturingState(ARMOR_ENTITY_GLINT_TEXTURING)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false));
    /**
     * @see RenderType#GLINT_TRANSLUCENT
     */
    private static final RenderType GLINT_TRANSLUCENT = create(SneakyCurses.id("glint_translucent").toString(),
            1536,
            GLINT_RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(TransformingPackResources.ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setTexturingState(GLINT_TEXTURING)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false));
    /**
     * @see RenderType#GLINT
     */
    private static final RenderType GLINT = create(SneakyCurses.id("glint").toString(),
            1536,
            GLINT_RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(TransformingPackResources.ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));
    /**
     * @see RenderType#ENTITY_GLINT
     */
    private static final RenderType ENTITY_GLINT = create(SneakyCurses.id("entity_glint").toString(),
            1536,
            GLINT_RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(TransformingPackResources.ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .createCompositeState(false));
    public static final Map<RenderType, RenderType> GLINT_RENDER_TYPES = ImmutableMap.of(RenderType.armorEntityGlint(),
            armorEntityGlint(),
            RenderType.glintTranslucent(),
            glintTranslucent(),
            RenderType.glint(),
            glint(),
            RenderType.entityGlint(),
            entityGlint());

    private ModRenderType(String string, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, i, bl, bl2, runnable, runnable2);
    }

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
}
