package com.fuzs.sneakymagic.common;

import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.mixin.accessor.IEnchantmentAccessor;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class CompatibilityManager {

    private final Map<Enchantment, EnchantmentType> defaultTypes = Maps.newHashMap();
    private final Map<Enchantment, Predicate<Item>> enchantmentPredicates = Maps.newHashMap();

    private int typeCounter;

    public void load() {

        this.sync(this.getTypesMap());
    }

    private Map<Set<Enchantment>, Predicate<Item>> getTypesMap() {

        final Map<Set<Enchantment>, Predicate<Item>> types = Maps.newHashMap();
        types.put(ConfigBuildHandler.swordEnchantments, item -> item instanceof SwordItem && !ConfigBuildHandler.swordBlacklist.contains(item));
        types.put(ConfigBuildHandler.axeEnchantments, item -> item instanceof AxeItem && !ConfigBuildHandler.axeBlacklist.contains(item));
        types.put(ConfigBuildHandler.tridentEnchantments, item -> item instanceof TridentItem && !ConfigBuildHandler.tridentBlacklist.contains(item));
        types.put(ConfigBuildHandler.bowEnchantments, item -> item instanceof BowItem && !ConfigBuildHandler.bowBlacklist.contains(item));
        types.put(ConfigBuildHandler.crossbowEnchantments, item -> item instanceof CrossbowItem && !ConfigBuildHandler.crossbowBlacklist.contains(item));

        return types;
    }

    private void sync(Map<Set<Enchantment>, Predicate<Item>> types) {

        types.forEach((key, value) -> key.forEach(enchantment -> {

            // absolutely have to save this in order to prevent a recursive loop and a StackOverflowError
            // probably necessary due to the way EnchantmentType#create behaves
            final EnchantmentType originalType = enchantment.type;
            final EnchantmentType combinedType = EnchantmentType.create(SneakyMagic.MODID.toUpperCase(Locale.ROOT) + "_TYPE_" + typeCounter++,
                    item -> originalType != null && originalType.canEnchantItem(item) || value.test(item));
            ((IEnchantmentAccessor) enchantment).setType(combinedType);
        }));
    }

    private Predicate<Item> getPredicate(Enchantment enchantment) {

        Optional<Predicate<Item>> predicate = Optional.ofNullable(this.enchantmentPredicates.get(enchantment));
        if (!predicate.isPresent()) {

            Predicate<Item> defaultPredicate = this.getDefaultPredicate(enchantment);
            this.enchantmentPredicates.put(enchantment, defaultPredicate);
            return defaultPredicate;
        }

        return predicate.get();
    }

    private Predicate<Item> getDefaultPredicate(Enchantment enchantment) {

        Optional<EnchantmentType> predicate = Optional.ofNullable(this.defaultTypes.get(enchantment));
        if (!predicate.isPresent()) {

            this.defaultTypes.put(enchantment, enchantment.type);
            return enchantment.type::canEnchantItem;
        }

        return predicate.get()::canEnchantItem;
    }

}
