package com.fuzs.sneakymagic.element;

import com.fuzs.puzzleslib_sm.PuzzlesLib;
import com.fuzs.puzzleslib_sm.capability.CapabilityController;
import com.fuzs.puzzleslib_sm.capability.core.CapabilityDispatcher;
import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.config.deserialize.EntryCollectionBuilder;
import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.capability.container.ArrowPlundering;
import com.fuzs.sneakymagic.capability.container.IArrowPlundering;
import com.fuzs.sneakymagic.mixin.accessor.IAbstractArrowEntityAccessor;
import com.fuzs.sneakymagic.util.CompatibilityManager;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Set;

public class CompatibilityElement extends AbstractElement implements ICommonElement {

    @ObjectHolder(SneakyMagic.MODID + ":" + "plundering")
    public static final Enchantment PLUNDERING_ENCHANTMENT = null;

    @CapabilityInject(IArrowPlundering.class)
    public static final Capability<ArrowPlundering> ARROW_PLUNDERING_CAPABILITY = null;
    
    public Set<Enchantment> swordEnchantments;
    public Set<Enchantment> axeEnchantments;
    public Set<Enchantment> tridentEnchantments;
    public Set<Enchantment> bowEnchantments;
    public Set<Enchantment> crossbowEnchantments;
    public Set<Item> swordBlacklist;
    public Set<Item> axeBlacklist;
    public Set<Item> tridentBlacklist;
    public Set<Item> bowBlacklist;
    public Set<Item> crossbowBlacklist;

    @Override
    public String getDescription() {

        return "Most vanilla enchantments can be applied to a lot more tools and weapons.";
    }

    @Override
    public void setupCommon() {

        EnchantmentType shootable = EnchantmentType.create("SHOOTABLE", item -> item instanceof BowItem || item instanceof CrossbowItem);
        PuzzlesLib.getRegistryManager().register("plundering", new LootBonusEnchantment(Enchantment.Rarity.RARE, shootable) {});
        this.addListener(this::onArrowLoose);
        this.addListener(this::onItemUseTick);
        this.addListener(this::onLootingLevel);
    }

    @Override
    public void loadCommon() {

        // register after config has been loaded once
        ConfigManager.get().addListener(new CompatibilityManager(this)::load, ModConfig.Type.COMMON);
        PuzzlesLib.getCapabilityController().addEntityCapability(new ResourceLocation(SneakyMagic.MODID, ArrowPlundering.getName()), IArrowPlundering.class, ArrowPlundering::new, entity -> {

            if (entity instanceof AbstractArrowEntity) {

                return new CapabilityDispatcher<>(new ArrowPlundering(), ARROW_PLUNDERING_CAPABILITY);
            }

            return null;
        });
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        String compatibility = "Additional enchantments to be made usable with ";
        String blacklist = " to be disabled from receiving additional enchantments.";
        addToConfig(builder.comment(compatibility + "swords.").define("Sword Enchantments", ConfigManager.getKeyList(Enchantments.IMPALING)), v -> this.swordEnchantments = deserializeToSet(v, ForgeRegistries.ENCHANTMENTS));
        addToConfig(builder.comment(compatibility + "axes.").define("Axe Enchantments", ConfigManager.getKeyList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.IMPALING)), v -> this.axeEnchantments = deserializeToSet(v, ForgeRegistries.ENCHANTMENTS));
        addToConfig(builder.comment(compatibility + "tridents.").define("Trident Enchantments", ConfigManager.getKeyList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.QUICK_CHARGE, Enchantments.PIERCING)), v -> this.tridentEnchantments = deserializeToSet(v, ForgeRegistries.ENCHANTMENTS));
        addToConfig(builder.comment(compatibility + "bows.").define("Bow Enchantments", ConfigManager.getKeyList(Enchantments.PIERCING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE)), v -> this.bowEnchantments = deserializeToSet(v, ForgeRegistries.ENCHANTMENTS));
        addToConfig(builder.comment(compatibility + "crossbows.").define("Crossbow Enchantments", ConfigManager.getKeyList(Enchantments.FLAME, Enchantments.PUNCH, Enchantments.POWER, Enchantments.INFINITY)), v -> this.crossbowEnchantments = deserializeToSet(v, ForgeRegistries.ENCHANTMENTS));
        addToConfig(builder.comment("Swords" + blacklist).define("Sword Blacklist", new ArrayList<String>()), v -> this.swordBlacklist = deserializeToSet(v, ForgeRegistries.ITEMS));
        addToConfig(builder.comment("Axes" + blacklist).define("Axe Blacklist", new ArrayList<String>()), v -> this.axeBlacklist = deserializeToSet(v, ForgeRegistries.ITEMS));
        addToConfig(builder.comment("Tridents" + blacklist).define("Trident Blacklist", new ArrayList<String>()), v -> this.tridentBlacklist = deserializeToSet(v, ForgeRegistries.ITEMS));
        addToConfig(builder.comment("Bows" + blacklist).define("Bow Blacklist", new ArrayList<String>()), v -> this.bowBlacklist = deserializeToSet(v, ForgeRegistries.ITEMS));
        addToConfig(builder.comment("Crossbows" + blacklist).define("Crossbow Blacklist", new ArrayList<String>()), v -> this.crossbowBlacklist = deserializeToSet(v, ForgeRegistries.ITEMS));
    }

    @Override
    public String[] getCommonDescription() {

        return new String[]{"Only enchantments included by default are guaranteed to work. While any modded enchantments or other vanilla enchantments can work, they are highly unlikely to do so.",
                "The blacklists for each item group are supposed to disable items which can be enchanted, but where the enchantments do not function as expected.",
                EntryCollectionBuilder.CONFIG_STRING};
    }

    private void onArrowLoose(final ArrowLooseEvent evt) {

        // multishot enchantment for bows
        ItemStack stack = evt.getBow();
        if (evt.hasAmmo() && EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) {

            float velocity = BowItem.getArrowVelocity(evt.getCharge());
            if ((double) velocity >= 0.1) {

                PlayerEntity playerentity = evt.getPlayer();
                ItemStack itemstack = playerentity.findAmmo(stack);
                ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

                for (int i = 0; i < 2; i++) {

                    AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(evt.getWorld(), itemstack, playerentity);
                    // shoot
                    abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw - 10.0F + i * 20.0F, 0.0F, velocity * 3.0F, 1.0F);
                    applyPowerEnchantment(abstractarrowentity, stack);
                    applyPunchEnchantment(abstractarrowentity, stack);
                    applyFlameEnchantment(abstractarrowentity, stack);
                    applyPiercingEnchantment(abstractarrowentity, stack);
                    applyPlunderingEnchantment(abstractarrowentity, stack);
                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    evt.getWorld().addEntity(abstractarrowentity);
                }
            }
        }
    }

    private void onItemUseTick(final LivingEntityUseItemEvent.Tick evt) {

        Item item = evt.getItem().getItem();
        int duration = evt.getItem().getUseDuration() - evt.getDuration();
        if (item instanceof BowItem && duration < 20 || item instanceof TridentItem && duration < 10) {

            // quick charge enchantment for bows and tridents
            int quickChargeLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, evt.getItem());
            evt.setDuration(evt.getDuration() - quickChargeLevel);
        }
    }

    private void onLootingLevel(final LootingLevelEvent evt) {

        if (evt.getDamageSource() != null) {

            Entity source = evt.getDamageSource().getImmediateSource();
            if (source instanceof AbstractArrowEntity) {

                if (source instanceof TridentEntity) {

                    ItemStack trident = ((IAbstractArrowEntityAccessor) source).callGetArrowStack();
                    int lootLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, trident);
                    if (lootLevel > 0) {

                        evt.setLootingLevel(lootLevel);
                    }
                } else {

                    // overwrite anything set by vanilla, even when enchantment is not present
                    evt.setLootingLevel(CapabilityController.getCapability(source, ARROW_PLUNDERING_CAPABILITY)
                            .map(ArrowPlundering::getPlunderingLevel)
                            .orElse((byte) 0));
                }
            }
        }
    }

    public static void applyPowerEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        if (powerLevel > 0) {

            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) powerLevel * 0.5 + 0.5);
        }
    }

    public static void applyPunchEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        if (punchLevel > 0) {

            abstractarrowentity.setKnockbackStrength(punchLevel);
        }
    }

    public static void applyFlameEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {

            abstractarrowentity.setFire(100);
        }
    }

    public static void applyPiercingEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int pierceLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, stack);
        if (pierceLevel > 0) {

            abstractarrowentity.setPierceLevel((byte) pierceLevel);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void applyPlunderingEnchantment(AbstractArrowEntity abstractarrowentity, ItemStack stack) {

        int plunderLevel = EnchantmentHelper.getEnchantmentLevel(PLUNDERING_ENCHANTMENT, stack);
        if (plunderLevel > 0) {

            CapabilityController.getCapability(abstractarrowentity, ARROW_PLUNDERING_CAPABILITY)
                    .ifPresent(cap -> cap.setPlunderingLevel((byte) plunderLevel));
        }
    }

}
