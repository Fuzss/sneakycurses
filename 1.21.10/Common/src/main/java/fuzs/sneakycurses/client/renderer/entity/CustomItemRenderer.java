package fuzs.sneakycurses.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.renderer.ModRenderType;
import fuzs.sneakycurses.config.ClientConfig;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.function.Function;

public class CustomItemRenderer {
    private static final ResourceLocation STANDARD_CURSE_LOCATION = SneakyCurses.id("standard_curse");
    private static final ResourceLocation SPECIAL_CURSE_LOCATION = SneakyCurses.id("special_curse");
    public static final ItemStackRenderState.FoilType STANDARD_CURSE_FOIL_TYPE = getEnumConstant(STANDARD_CURSE_LOCATION,
            ItemStackRenderState.FoilType::valueOf);
    public static final ItemStackRenderState.FoilType SPECIAL_CURSE_FOIL_TYPE = getEnumConstant(SPECIAL_CURSE_LOCATION,
            ItemStackRenderState.FoilType::valueOf);

    public static boolean isCurseFoilType(ItemStackRenderState.FoilType foilType) {
        return foilType == STANDARD_CURSE_FOIL_TYPE || foilType == SPECIAL_CURSE_FOIL_TYPE;
    }

    public static boolean isItemStackCursed(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasFoil()) {
            return false;
        } else if (!SneakyCurses.CONFIG.getHolder(ServerConfig.class).isAvailable() || !SneakyCurses.CONFIG.get(
                ServerConfig.class).cursedItemGlint) {
            return false;
        } else {
            return CurseRevealHandler.anyEnchantIsCursed(itemStack);
        }
    }

    /**
     * @see net.minecraft.client.renderer.entity.ItemRenderer#getSpecialFoilBuffer(MultiBufferSource, RenderType,
     *         PoseStack.Pose)
     */
    public static VertexConsumer getSpecialFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, PoseStack.Pose pose) {
        return VertexMultiConsumer.create(new SheetedDecalTextureGenerator(getFoilBuffer(bufferSource.getBuffer(
                ItemRenderer.useTransparentGlint(renderType) ? ModRenderType.glintTranslucent() :
                        ModRenderType.glint())), pose, 0.0078125F), bufferSource.getBuffer(renderType));
    }

    /**
     * @see net.minecraft.client.renderer.entity.ItemRenderer#getFoilBuffer(MultiBufferSource, RenderType, boolean,
     *         boolean)
     */
    public static VertexConsumer getFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, boolean isItem, boolean glint) {
        if (glint) {
            return ItemRenderer.useTransparentGlint(renderType) ?
                    VertexMultiConsumer.create(getFoilBuffer(bufferSource.getBuffer(ModRenderType.glintTranslucent())),
                            bufferSource.getBuffer(renderType)) :
                    VertexMultiConsumer.create(getFoilBuffer(bufferSource.getBuffer(
                                    isItem ? ModRenderType.glint() : ModRenderType.entityGlint())),
                            bufferSource.getBuffer(renderType));
        } else {
            return bufferSource.getBuffer(renderType);
        }
    }

    public static VertexConsumer getFoilBuffer(VertexConsumer vertexConsumer) {
        return new OutlineBufferSource.EntityOutlineGenerator(vertexConsumer,
                ARGB.opaque(SneakyCurses.CONFIG.get(ClientConfig.class).cursedGlintColor.getTextureDiffuseColor()));
    }

    private static <E extends Enum<E>> E getEnumConstant(ResourceLocation resourceLocation, Function<String, E> valueOfInvoker) {
        return valueOfInvoker.apply(resourceLocation.toDebugFileName().toUpperCase(Locale.ROOT));
    }
}
