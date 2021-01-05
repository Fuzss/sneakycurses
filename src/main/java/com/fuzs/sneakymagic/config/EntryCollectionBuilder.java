package com.fuzs.sneakymagic.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * builds a collection for a given type of registry from a list of strings
 * @param <T> content type of collection to build
 */
@SuppressWarnings("unused")
public class EntryCollectionBuilder<T extends IForgeRegistryEntry<T>> extends StringListParser<T> {

    /**
     * @param registry registry entries the to be created collections contain
     */
    public EntryCollectionBuilder(IForgeRegistry<T> registry) {

        super(registry);
    }

    /**
     * @param locations resource locations to build set from
     * @return entry set associated with given resource locations in active registry
     */
    public Set<T> buildEntrySet(List<String> locations) {

        return this.buildEntrySetWithCondition(locations, flag -> true, "");
    }

    /**
     * @param locations resource locations to build set from
     * @return entry map associated with given resource locations in active registry paired with a given double value
     */
    public Map<T, Double> buildEntryMap(List<String> locations) {

        return this.buildEntryMapWithCondition(locations, (entry, value) -> true, "");
    }

    /**
     * @param locations resource locations to build set from
     * @param condition condition need to match for an entry to be added to the set
     * @param message message to be logged when condition is not met
     * @return entry set associated with given resource locations in active registry
     */
    public Set<T> buildEntrySetWithCondition(List<String> locations, Predicate<T> condition, String message) {

        Set<T> set = Sets.newHashSet();
        for (String source : locations) {

            this.getEntriesFromRegistry(source.trim()).forEach(entry -> {

                if (condition.test(entry)) {

                    if (this.isNotPresent(set, entry)) {

                        set.add(entry);
                    }
                } else {

                    log(source, message);
                }
            });
        }

        return set;
    }

    /**
     * @param locations resource locations to build set from
     * @param condition condition need to match for an entry to be added to the map
     * @param message message to be logged when condition is not met
     * @return entry map associated with given resource locations in active registry paired with a given double value
     */
    public Map<T, Double> buildEntryMapWithCondition(List<String> locations, BiPredicate<T, Double> condition, String message) {

        Map<T, Double> map = Maps.newHashMap();
        for (String source : locations) {

            String[] splitSource = Stream.of(source.split(",")).map(String::trim).toArray(String[]::new);
            if (splitSource.length == 2) {

                List<T> entries = this.getEntriesFromRegistry(splitSource[0]);
                if (entries.isEmpty()) {

                    continue;
                }

                parseDouble(splitSource[1], source).ifPresent(value -> entries.forEach(entry -> {

                    if (condition.test(entry, value)) {

                        if (this.isNotPresent(map.keySet(), entry)) {

                            map.put(entry, value);
                        }
                    } else {

                        log(source, message);
                    }
                }));
            } else {

                log(source, "Insufficient number of arguments");
            }
        }

        return map;
    }

    private static Optional<Double> parseDouble(String value, String source) {

        try {

            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException ignored) {

            log(source, "Invalid number format");
        }

        return Optional.empty();
    }

}
