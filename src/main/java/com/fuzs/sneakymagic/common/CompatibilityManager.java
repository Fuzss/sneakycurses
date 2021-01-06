package com.fuzs.sneakymagic.common;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.mixin.accessor.IEnchantmentAccessor;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class CompatibilityManager {

    private final Map<Enchantment, EnchantmentType> defaultTypes = Maps.newHashMap();
    private final Set<Set<Enchantment>> previousEnchantments = Sets.newHashSet();
    private final Map<Enchantment, Predicate<Item>> enchantmentPredicates = Maps.newHashMap();

    private int typeCounter;

    public void load() {

        Map<Set<Enchantment>, Predicate<Item>> configMap = this.createConfigMap();
        if (this.isConfigChanged(configMap)) {

            this.createPredicatesMap(configMap);
            this.setEnchantmentTypes(this.enchantmentPredicates);
            this.enchantmentPredicates.clear();
        }
    }

    private Map<Set<Enchantment>, Predicate<Item>> createConfigMap() {

        final Map<Set<Enchantment>, Predicate<Item>> predicates = Maps.newHashMap();
        predicates.put(ConfigBuildHandler.swordEnchantments, item -> item instanceof SwordItem && !ConfigBuildHandler.swordBlacklist.contains(item));
        predicates.put(ConfigBuildHandler.axeEnchantments, item -> item instanceof AxeItem && !ConfigBuildHandler.axeBlacklist.contains(item));
        predicates.put(ConfigBuildHandler.tridentEnchantments, item -> item instanceof TridentItem && !ConfigBuildHandler.tridentBlacklist.contains(item));
        predicates.put(ConfigBuildHandler.bowEnchantments, item -> item instanceof BowItem && !ConfigBuildHandler.bowBlacklist.contains(item));
        predicates.put(ConfigBuildHandler.crossbowEnchantments, item -> item instanceof CrossbowItem && !ConfigBuildHandler.crossbowBlacklist.contains(item));

        return predicates;
    }

    private boolean isConfigChanged(Map<Set<Enchantment>, Predicate<Item>> configMap) {

        Set<Set<Enchantment>> currentEnchantments = configMap.keySet();
        if (!currentEnchantments.equals(this.previousEnchantments)) {

            this.previousEnchantments.clear();
            this.previousEnchantments.addAll(currentEnchantments);
            return true;
        }

        return false;
    }

    private void createPredicatesMap(Map<Set<Enchantment>, Predicate<Item>> configMap) {

        configMap.forEach((key, value) -> key.forEach(enchantment -> this.putPredicate(enchantment, value)));
    }

    private void putPredicate(Enchantment enchantment, Predicate<Item> predicate) {

        this.enchantmentPredicates.computeIfAbsent(enchantment, this::getDefaultPredicate);
        this.enchantmentPredicates.merge(enchantment, predicate, ((predicate1, predicate2) -> (item -> predicate1.test(item) || predicate2.test(item))));
    }

    private Predicate<Item> getDefaultPredicate(Enchantment enchantment) {

        return this.defaultTypes.computeIfAbsent(enchantment, key -> key.type)::canEnchantItem;
    }

    private void setEnchantmentTypes(Map<Enchantment, Predicate<Item>> enchantmentPredicates) {

        enchantmentPredicates.forEach((key, value) -> {

//            System.out.println(ForgeRegistries.ENCHANTMENTS.getKey(key).toString() + ": " + this.typeCounter);
            final EnchantmentType combinedType = EnchantmentType.create(SneakyMagic.MODID.toUpperCase(Locale.ROOT) + "_TYPE_" + typeCounter++, value);
            ((IEnchantmentAccessor) key).setType(combinedType);
        });
    }

}
