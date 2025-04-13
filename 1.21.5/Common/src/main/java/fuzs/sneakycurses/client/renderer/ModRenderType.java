package fuzs.sneakycurses.client.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

public abstract class ModRenderType extends RenderType {
    static final ResourceLocation ENCHANTED_GLINT_ARMOR = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ARMOR.getPath());
    static final ResourceLocation ENCHANTED_GLINT_ITEM = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ITEM.getPath());
    /**
     * @see RenderType#ARMOR_ENTITY_GLINT
     */
    private static final RenderType ARMOR_ENTITY_GLINT = create(SneakyCurses.id("armor_entity_glint").toString(),
            1536,
            RenderPipelines.GLINT,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ARMOR,
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
            RenderPipelines.GLINT,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM,
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
            RenderPipelines.GLINT,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));
    /**
     * @see RenderType#ENTITY_GLINT
     */
    private static final RenderType ENTITY_GLINT = create(SneakyCurses.id("entity_glint").toString(),
            1536,
            RenderPipelines.GLINT,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM,
                            TriState.DEFAULT,
                            false))
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
