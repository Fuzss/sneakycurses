package com.fuzs.sneakymagic.common.element;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.sneakymagic.SneakyMagic;
import com.google.common.collect.Maps;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class AnvilTweaksElement extends AbstractElement implements ICommonElement {

    public static final Tags.IOptionalNamedTag<Item> ANVIL_REPAIR_MATERIALS = ItemTags.createOptional(new ResourceLocation(SneakyMagic.MODID, "anvil_repair_materials"));

    public final Map<ResourceLocation, Tags.IOptionalNamedTag<Item>> repairMaterialTags = Maps.newHashMap();

    private boolean repairAnvilWithIron;
    private int anvilRepairChance;
    public boolean moreRepairMaterials;

    @Override
    public String getDescription() {

        return "Some convenient tweaks for working with anvils.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onRightClickBlock);
    }

    @Override
    public void loadCommon() {

        if (this.moreRepairMaterials) {

            ForgeRegistries.ITEMS.getValues().stream()
                    .filter(Item::isDamageable)
                    .map(item -> Objects.requireNonNull(item.getRegistryName()))
                    .forEach(location -> this.repairMaterialTags.put(location, ItemTags.createOptional(new ResourceLocation(location.getNamespace(), "repair_materials/" + location.getPath()))));
        }
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Using an iron ingot on a damaged anvil has a chance to repair it.").define("Iron Repairs Anvil", true), v -> this.repairAnvilWithIron = v);
        addToConfig(builder.comment("Chance one out of set value an attempt at repairing an anvil will be successful.").defineInRange("Anvil Repair Chance", 3, 1, Integer.MAX_VALUE), v -> this.anvilRepairChance = v);
        addToConfig(builder.comment("Materials for repairing items in an anvil can be provided and overwritten by item tags. By default, materials are added for trident, bow, crossbow, shears, flint and steel, fishing rod, and elytra.").define("More Repair Materials", true), v -> this.moreRepairMaterials = v);
    }

    private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock evt) {

        if (this.repairAnvilWithIron && evt.getSide().isServer() && evt.getItemStack().getItem().isIn(ANVIL_REPAIR_MATERIALS)) {

            World world = evt.getWorld();
            BlockPos blockPosIn = evt.getPos();
            BlockState blockState = repair(world.getBlockState(blockPosIn));
            if (blockState != null) {

                evt.setUseBlock(Event.Result.DENY);
                evt.setUseItem(Event.Result.ALLOW);
                if (!evt.getPlayer().abilities.isCreativeMode) {

                    evt.getItemStack().shrink(1);
                }

                if (world.getRandom().nextInt(this.anvilRepairChance) == 0) {

                    world.setBlockState(blockPosIn, blockState, 2);
                    // play repair sound
                    world.playEvent(Constants.WorldEvents.ANVIL_USE_SOUND, blockPosIn, 0);
                    // show block breaking particles for anvil
                    world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, blockPosIn, Block.getStateId(blockState));
                } else if (world instanceof ServerWorld) {

                    double randomOffset = 0.33;
                    double posX = blockPosIn.getX() + 0.5;
                    double posY = blockPosIn.getY() + 0.5;
                    double posZ = blockPosIn.getZ() + 0.5;
                    ((ServerWorld) world).spawnParticle(ParticleTypes.SMOKE, posX, posY, posZ, 20, randomOffset, randomOffset, randomOffset, 0.0);
                }
            }
        }
    }

    @Nullable
    private static BlockState repair(BlockState state) {

        if (state.isIn(Blocks.DAMAGED_ANVIL)) {

            return Blocks.CHIPPED_ANVIL.getDefaultState().with(AnvilBlock.FACING, state.get(AnvilBlock.FACING));
        } else {

            return state.isIn(Blocks.CHIPPED_ANVIL) ? Blocks.ANVIL.getDefaultState().with(AnvilBlock.FACING, state.get(AnvilBlock.FACING)) : null;
        }
    }

}