package fuzs.sneakycurses.client.renderer.rendertype;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.packs.TransformingPackResources;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.*;

import java.util.Map;

/**
 * Our render pipeline uses a custom glint shader that respects the color parameters passed to a vertex consumer.
 * <p>
 * The render types use a dynamically generated copy of the glint textures that is converted to grayscale and brightened
 * to support dying via a custom vertex consumer implementation at
 * {@link net.minecraft.client.renderer.OutlineBufferSource.EntityOutlineGenerator}.
 */
public final class ModRenderTypes {
    /**
     * @see RenderPipelines#GLINT
     */
    public static final RenderPipeline GLINT_RENDER_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET,
                    RenderPipelines.FOG_SNIPPET,
                    RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(SneakyCurses.id("pipeline/glint"))
            .withVertexShader(SneakyCurses.id("core/glint"))
            .withFragmentShader(SneakyCurses.id("core/glint"))
            .withSampler("Sampler0")
            .withDepthWrite(false)
            .withCull(false)
            .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
            .withBlend(BlendFunction.GLINT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();
    /**
     * We use the item glint texture as it looks much better.
     *
     * @see RenderTypes#ARMOR_ENTITY_GLINT
     */
    private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create(SneakyCurses.id("armor_entity_glint")
                    .toString(),
            RenderSetup.builder(GLINT_RENDER_PIPELINE)
                    .withTexture("Sampler0", TransformingPackResources.ENCHANTED_GLINT_ITEM)
                    .setTextureTransform(TextureTransform.ARMOR_ENTITY_GLINT_TEXTURING)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup());
    /**
     * @see RenderTypes#GLINT_TRANSLUCENT
     */
    private static final RenderType GLINT_TRANSLUCENT = RenderType.create(SneakyCurses.id("glint_translucent")
                    .toString(),
            RenderSetup.builder(GLINT_RENDER_PIPELINE)
                    .withTexture("Sampler0", TransformingPackResources.ENCHANTED_GLINT_ITEM)
                    .setTextureTransform(TextureTransform.GLINT_TEXTURING)
                    .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                    .createRenderSetup());
    /**
     * @see RenderTypes#GLINT
     */
    private static final RenderType GLINT = RenderType.create(SneakyCurses.id("glint").toString(),
            RenderSetup.builder(GLINT_RENDER_PIPELINE)
                    .withTexture("Sampler0", TransformingPackResources.ENCHANTED_GLINT_ITEM)
                    .setTextureTransform(TextureTransform.GLINT_TEXTURING)
                    .createRenderSetup());
    /**
     * @see RenderTypes#ENTITY_GLINT
     */
    private static final RenderType ENTITY_GLINT = RenderType.create(SneakyCurses.id("entity_glint").toString(),
            RenderSetup.builder(GLINT_RENDER_PIPELINE)
                    .withTexture("Sampler0", TransformingPackResources.ENCHANTED_GLINT_ITEM)
                    .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                    .createRenderSetup());
    public static final Map<RenderType, RenderType> GLINT_RENDER_TYPES = ImmutableMap.of(RenderTypes.armorEntityGlint(),
            armorEntityGlint(),
            RenderTypes.glintTranslucent(),
            glintTranslucent(),
            RenderTypes.glint(),
            glint(),
            RenderTypes.entityGlint(),
            entityGlint());

    private ModRenderTypes() {
        // NO-OP
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
