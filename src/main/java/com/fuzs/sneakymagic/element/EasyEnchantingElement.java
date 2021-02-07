package com.fuzs.sneakymagic.element;

import com.fuzs.puzzleslib_sm.PuzzlesLib;
import com.fuzs.puzzleslib_sm.element.extension.ClientExtensibleElement;
import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.client.element.EasyEnchantingExtension;
import com.fuzs.sneakymagic.inventory.container.EnchantmentInventoryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.LootBonusEnchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;

public class EasyEnchantingElement extends ClientExtensibleElement<EasyEnchantingExtension> {

//    @ObjectHolder(SneakyMagic.MODID + ":" + "enchantment_inventory")
//    public static final ContainerType<EnchantmentInventoryContainer> ENCHANTMENT_INVENTORY = null;

    public boolean itemsStay;
    public boolean rerollEnchantments;

    public EasyEnchantingElement() {

        super(element -> new EasyEnchantingExtension((EasyEnchantingElement) element));
    }

    @Override
    public String getDescription() {

        return "Small improvements for making working with an enchantment table more fun.";
    }

    @Override
    public void setupCommon() {

//        PuzzlesLib.getRegistryManager().register("enchantment_inventory", IForgeContainerType.create(EnchantmentInventoryContainer::new));
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Inventory contents stay in their slot after closing the enchanting screen. Also makes hoppers able to input and output items.").define("Contents Stay", true), v -> this.itemsStay = v);
        addToConfig(builder.comment("Reroll possible enchantments in an enchanting table every time an item is placed into the enchanting slot.").define("Reroll Enchantments", true), v -> this.rerollEnchantments = v);
    }

    public static Rarity getFutureRarity(ItemStack stack) {

        if (stack.getItem() == Items.BOOK) {

            return Rarity.UNCOMMON;
        } else {

            switch (stack.getRarity()) {

                case COMMON:
                case UNCOMMON:

                    return Rarity.RARE;
                case RARE:

                    return Rarity.EPIC;
                case EPIC:
                default:

                    return stack.getRarity();
            }
        }
    }

}
