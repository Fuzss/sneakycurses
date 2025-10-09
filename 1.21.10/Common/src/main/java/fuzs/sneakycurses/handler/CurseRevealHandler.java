package fuzs.sneakycurses.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.init.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public class CurseRevealHandler {
    public static final String KEY_ITEM_CURSES_REVEALED = Util.makeDescriptionId(Registries.elementsDirPath(Registries.ITEM),
            SneakyCurses.id("curses_revealed"));

    public static EventResult onCreateAnvilResult(Player player, ItemStack primaryItemStack, ItemStack secondaryItemStack, MutableValue<ItemStack> outputItemStack, @Nullable String itemName, MutableInt enchantmentLevelCost, MutableInt repairMaterialCost) {
        if (isAffected(primaryItemStack) && secondaryItemStack.is(ModRegistry.REVEALS_CURSES_ITEM_TAG)
                && !allCursesRevealed(primaryItemStack)) {
            ItemStack itemStack = primaryItemStack.copy();
            revealAllCurses(itemStack);
            outputItemStack.accept(itemStack);
            repairMaterialCost.accept(1);
            enchantmentLevelCost.accept(SneakyCurses.CONFIG.get(ServerConfig.class).revealCursesCost);
            return EventResult.ALLOW;
        } else {
            return EventResult.PASS;
        }
    }

    public static void onEndEntityTick(Entity entity) {
        if (!entity.level().isClientSide() && entity.tickCount % 1200 == 0
                && entity instanceof LivingEntity livingEntity) {
            if (!(entity instanceof Player player) || !player.getAbilities().invulnerable) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
                    if (livingEntity.getEquipmentSlotForItem(itemStack) == equipmentSlot
                            && anyEnchantIsCursed(itemStack) && !allCursesRevealed(itemStack)) {
                        if (entity.getRandom().nextDouble()
                                < SneakyCurses.CONFIG.get(ServerConfig.class).curseRevealChance) {
                            revealAllCurses(itemStack);
                            entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE,
                                    1.0F,
                                    entity.getRandom().nextFloat() * 0.1F + 0.9F);
                            if (entity instanceof Player player) {
                                player.displayClientMessage(Component.translatable(KEY_ITEM_CURSES_REVEALED,
                                        itemStack.getDisplayName()).withStyle(ChatFormatting.DARK_PURPLE), false);
                            }

                            break;
                        }
                    }
                }
            }
        }
    }

    public static void revealAllCurses(ItemStack itemStack) {
        if (anyEnchantIsCursed(itemStack)) {
            itemStack.set(ModRegistry.REVEAL_CURSES_DATA_COMPONENT_TYPE.value(), Unit.INSTANCE);
        }
    }

    public static boolean allCursesRevealed(ItemStack itemStack) {
        return itemStack.has(ModRegistry.REVEAL_CURSES_DATA_COMPONENT_TYPE.value());
    }

    public static boolean anyEnchantIsCursed(ItemStack itemStack) {
        return !itemStack.isEmpty() && EnchantmentHelper.getEnchantmentsForCrafting(itemStack)
                .keySet()
                .stream()
                .anyMatch((Holder<Enchantment> holder) -> holder.is(EnchantmentTags.CURSE));
    }

    public static boolean isAffected(ItemStack itemStack) {
        if (itemStack.is(Items.ENCHANTED_BOOK) && !SneakyCurses.CONFIG.get(ServerConfig.class).affectBooks) {
            return false;
        } else {
            return anyEnchantIsCursed(itemStack);
        }
    }
}
