package com.fuzs.sneakymagic.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ConfigBuilder {

    private static final EnumMap<ModConfig.Type, ForgeConfigSpec.Builder> BUILDERS = Stream.of(ModConfig.Type.values()).collect(Collectors.toMap(Function.identity(), key -> new ForgeConfigSpec.Builder(), (key1, key2) -> key1, () -> new EnumMap<>(ModConfig.Type.class)));
    private static final EnumMap<ModConfig.Type, ForgeConfigSpec> SPECS = new EnumMap<>(ModConfig.Type.class);

    private static ModConfig.Type activeType;

    private ConfigBuilder() {

    }

    public static ForgeConfigSpec getCommonSpec() {

        return getSpec(ModConfig.Type.COMMON);
    }

    public static ForgeConfigSpec getClientSpec() {

        return getSpec(ModConfig.Type.CLIENT);
    }

    public static ForgeConfigSpec getServerSpec() {

        return getSpec(ModConfig.Type.SERVER);
    }

    private static ForgeConfigSpec getSpec(ModConfig.Type type) {

        return SPECS.computeIfAbsent(type, key -> getBuilder(key).build());
    }

    public static boolean isSpecNotBuilt(ModConfig.Type type) {

        return !SPECS.containsKey(type);
    }

    public static boolean isSpecNotLoaded(ModConfig.Type type) {

        return SPECS.get(type).isLoaded();
    }

    public static boolean addSpec(ForgeConfigSpec spec, ModConfig.Type type) {

        if (isSpecNotBuilt(type)) {

            SPECS.put(type, spec);
            return true;
        }

        return false;
    }

    private static ForgeConfigSpec.Builder getBuilder(ModConfig.Type type) {

        return BUILDERS.get(type);
    }

    public static void createCategory(String name, Consumer<ForgeConfigSpec.Builder> options, ModConfig.Type type, String... comments) {

        activeType = type;

        ForgeConfigSpec.Builder builder = BUILDERS.get(type);
        if (comments.length != 0) {

            builder.comment(comments);
        }

        builder.push(name);
        options.accept(builder);
        builder.pop();

        activeType = null;
    }

    public static ModConfig.Type getActiveType() {

        return activeType;
    }

}
