package com.fuzs.sneakymagic.asm;

import com.fuzs.sneakymagic.common.CompatibilityHandler;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"unused", "JavadocReference"})
public class Hooks {

    /**
     * makes it possible to apply infinity and mending together in {@link net.minecraft.enchantment.InfinityEnchantment#canApplyTogether}
     */
    public static boolean canApplyMending(boolean flag) {

        return !ConfigBuildHandler.INFINITY_MENDING_FIX.get() && flag;
    }

    /**
     * makes it possible to apply multishot and piercing together in {@link net.minecraft.enchantment.PiercingEnchantment#canApplyTogether}
     */
    public static boolean canApplyPiercing(boolean flag, Enchantment ench) {

        return flag && (ConfigBuildHandler.MULTISHOT_PIERCING_FIX.get() || ench != Enchantments.MULTISHOT);
    }

    /**
     * makes it possible to apply multishot and piercing together in {@link net.minecraft.enchantment.MultishotEnchantment#canApplyTogether}
     */
    public static boolean canApplyMultishot(boolean flag, Enchantment ench) {

        return flag && (ConfigBuildHandler.MULTISHOT_PIERCING_FIX.get() || ench != Enchantments.PIERCING);
    }

    /**
     * makes different types of protection compatible with each other in {@link net.minecraft.enchantment.ProtectionEnchantment#canApplyTogether}
     */
    public static boolean canApplyProtection() {

        return !ConfigBuildHandler.PROTECTION_FIX.get();
    }

    /**
     * generates ammo out of thin air in {@link net.minecraft.item.CrossbowItem#hasAmmo}
     */
    public static boolean hasInfinity(boolean flag, ItemStack stack) {

        return flag || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
    }

    /**
     * add bow enchantments to crossbow arrow and properly set infinity pickup status in {@link net.minecraft.item.CrossbowItem#createArrow}
     */
    public static void applyCrossbowEnchantments(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        CompatibilityHandler.applyCrossbowEnchantments(abstractarrowentity, stack);
    }

    /**
     * checks a few more curse related conditions to see if the item effect should be rendered in {@link net.minecraft.item.Item#hasEffect}
     */
    public static boolean hasEffect(boolean isEnchanted, ItemStack stack) {

        return isEnchanted && !(EnchantmentHelper.func_226652_a_(stack.getEnchantmentTagList()).keySet().stream().filter(Objects::nonNull)
                .allMatch(Enchantment::isCurse) && ConfigBuildHandler.HIDE_GLINT.get());
    }

    /**
     * switch "enchanted_item_glint.png" with a colored version in multiple places
     */
    public static void setupGlintRenderer(ItemStack stack, boolean isItem) {

        if (!stack.hasEffect() || ConfigBuildHandler.GLINT_COLOR.get().isDefault() && ConfigBuildHandler.CURSED_GLINT_COLOR.get().isDefault()) {

            return;
        }

        boolean isCursed = EnchantmentHelper.func_226652_a_(stack.getEnchantmentTagList()).keySet().stream().filter(Objects::nonNull).anyMatch(Enchantment::isCurse);
        setupGlintRenderer(isCursed, isItem);
    }

    @SuppressWarnings("ConstantConditions")
    private static void setupGlintRenderer(boolean isCursed, boolean isItem) {

        ResourceLocation location = isCursed ? ConfigBuildHandler.CURSED_GLINT_COLOR.get().getLocation() : ConfigBuildHandler.GLINT_COLOR.get().getLocation();
        RenderType.Type type = (RenderType.Type) (isItem ? RenderType.GLINT : RenderType.ENTITY_GLINT);

        type.renderState.texture.setupTask = () -> {

            RenderSystem.enableTexture();
            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
            texturemanager.bindTexture(location);
            texturemanager.getTexture(location).setBlurMipmapDirect(true, false);
        };
        type.renderState.texture.texture = Optional.of(location);


    }

    public static void setupGlintRenderer(boolean hasEffect) {

        if (!hasEffect || ConfigBuildHandler.GLINT_COLOR.get().isDefault() && ConfigBuildHandler.CURSED_GLINT_COLOR.get().isDefault()) {

            return;
        }

        setupGlintRenderer(false, false);
    }

}
