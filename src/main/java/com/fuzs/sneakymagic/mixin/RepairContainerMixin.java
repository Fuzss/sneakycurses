package com.fuzs.sneakymagic.mixin;

import com.fuzs.sneakymagic.common.SneakyMagicElements;
import com.fuzs.sneakymagic.common.element.AnvilTweaksElement;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("unused")
@Mixin(RepairContainer.class)
public abstract class RepairContainerMixin extends AbstractRepairContainer {

    public RepairContainerMixin(@Nullable ContainerType<?> p_i231587_1_, int p_i231587_2_, PlayerInventory p_i231587_3_, IWorldPosCallable p_i231587_4_) {

        super(p_i231587_1_, p_i231587_2_, p_i231587_3_, p_i231587_4_);
    }

    @Redirect(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getIsRepairable(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    public boolean getIsRepairable(Item item, ItemStack toRepair, ItemStack repair) {

        AnvilTweaksElement element = SneakyMagicElements.getAs(SneakyMagicElements.ANVIL_TWEAKS);
        if (element.isEnabled() && element.moreRepairMaterials) {

            Tags.IOptionalNamedTag<Item> tag = element.repairMaterialTags.get(Objects.requireNonNull(toRepair.getItem().getRegistryName()));
            if (tag != null && !tag.getAllElements().isEmpty()) {

                return repair.getItem().isIn(tag);
            }
        }

        return item.getIsRepairable(toRepair, repair);
    }



}
