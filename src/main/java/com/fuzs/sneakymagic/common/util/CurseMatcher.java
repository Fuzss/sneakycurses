package com.fuzs.sneakymagic.common.util;

import net.minecraft.enchantment.Enchantment;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CurseMatcher {

    private static final Predicate<Enchantment> CURSE_PREDICATE = Enchantment::isCurse;

    public static boolean anyMatch(Collection<Enchantment> enchantments) {

        return createNonNullStream(enchantments).anyMatch(CURSE_PREDICATE);
    }

    public static boolean allMatch(Collection<Enchantment> enchantments) {

        return createNonNullStream(enchantments).allMatch(CURSE_PREDICATE);
    }

    public static boolean noneMatch(Collection<Enchantment> enchantments) {

        return createNonNullStream(enchantments).noneMatch(CURSE_PREDICATE);
    }

    private static Stream<Enchantment> createNonNullStream(Collection<Enchantment> enchantments) {

        return enchantments.stream().filter(Objects::nonNull);
    }

}
