package fuzs.sneakycurses.client.packs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.puzzleslib.api.client.packs.v1.NativeImageHelper;
import fuzs.puzzleslib.api.resources.v1.AbstractModPackResources;
import fuzs.puzzleslib.api.util.v1.HSV;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TransformingPackResources extends AbstractModPackResources {
    public static final ResourceLocation ENCHANTED_GLINT_ARMOR = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ARMOR.getPath());
    public static final ResourceLocation ENCHANTED_GLINT_ITEM = SneakyCurses.id(ItemRenderer.ENCHANTED_GLINT_ITEM.getPath());
    private static final Map<ResourceLocation, ResourceLocation> RESOURCE_LOCATIONS;

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

    static {
        ImmutableMap.Builder<ResourceLocation, ResourceLocation> builder = ImmutableMap.builder();
        registerTextureMapping(builder::put, ItemRenderer.ENCHANTED_GLINT_ARMOR, ENCHANTED_GLINT_ARMOR);
        registerTextureMapping(builder::put, ItemRenderer.ENCHANTED_GLINT_ITEM, ENCHANTED_GLINT_ITEM);
        RESOURCE_LOCATIONS = builder.build();
    }

    protected static void registerTextureMapping(BiConsumer<ResourceLocation, ResourceLocation> consumer, ResourceLocation originalResourceLocation, ResourceLocation providedResourceLocation) {
        consumer.accept(providedResourceLocation, originalResourceLocation);
        consumer.accept(providedResourceLocation.withSuffix(".mcmeta"), originalResourceLocation.withSuffix(".mcmeta"));
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if (RESOURCE_LOCATIONS.containsKey(resourceLocation)) {
            Optional<Resource> optional = this.resourceManager.getResource(RESOURCE_LOCATIONS.get(resourceLocation));
            if (optional.isPresent()) {
                if (resourceLocation.getPath().endsWith(".png")) {
                    try (NativeImage nativeImage = NativeImage.read(optional.get().open())) {
                        for (int x = 0; x < nativeImage.getWidth(); x++) {
                            for (int y = 0; y < nativeImage.getHeight(); y++) {
                                int pixel = nativeImage.getPixel(x, y);
                                int alpha = ARGB.alpha(pixel);
                                if (alpha != 0) {
                                    nativeImage.setPixel(x, y, this.applyPixelTransformation(pixel));
                                }
                            }
                        }
                        byte[] byteArray = NativeImageHelper.asByteArray(nativeImage);
                        return () -> new ByteArrayInputStream(byteArray);
                    } catch (IOException ignored) {
                        // NO-OP
                    }
                }

                return optional.get()::open;
            } else {
                return null;
            }
        } else {
            return super.getResource(packType, resourceLocation);
        }
    }

    private int applyPixelTransformation(int pixel) {
        int greyscaleColor = ARGB.greyscale(pixel);
        int hsvColor = HSV.rgbToHsv(ARGB.redFloat(greyscaleColor),
                ARGB.greenFloat(greyscaleColor),
                ARGB.blueFloat(greyscaleColor));
        // just some factor to brighten this up, values between 2.0 to 3.0 seem to work well,
        // as the vanilla glint texture has a value of about 0.2 on average,
        // although higher values will make colors close to white appear less translucent
        int rgbColor = HSV.hsvToRgb(HSV.hueFloat(hsvColor),
                HSV.saturationFloat(hsvColor),
                Math.min(1.0F, HSV.valueFloat(hsvColor) * 2.5F));
        return ARGB.color(ARGB.alpha(pixel), rgbColor);
    }
}
