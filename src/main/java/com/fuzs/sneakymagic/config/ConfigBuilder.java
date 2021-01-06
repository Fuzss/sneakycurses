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

    private final EnumMap<ModConfig.Type, ForgeConfigSpec> specs = new EnumMap<>(ModConfig.Type.class);
    private final EnumMap<ModConfig.Type, ForgeConfigSpec.Builder> builders = Stream.of(ModConfig.Type.values())
            .collect(Collectors.toMap(Function.identity(), key -> new ForgeConfigSpec.Builder(), (key1, key2) -> key1, () -> new EnumMap<>(ModConfig.Type.class)));

    private ModConfig.Type activeType;

    public ForgeConfigSpec getCommonSpec() {

        return this.getSpec(ModConfig.Type.COMMON);
    }

    public ForgeConfigSpec getClientSpec() {

        return this.getSpec(ModConfig.Type.CLIENT);
    }

    public ForgeConfigSpec getServerSpec() {

        return this.getSpec(ModConfig.Type.SERVER);
    }

    private ForgeConfigSpec getSpec(ModConfig.Type type) {

        return this.specs.computeIfAbsent(type, key -> getBuilder(key).build());
    }

    public boolean isSpecNotBuilt(ModConfig.Type type) {

        return !this.specs.containsKey(type);
    }

    public boolean isSpecNotLoaded(ModConfig.Type type) {

        return !this.specs.get(type).isLoaded();
    }

    public boolean addSpec(ForgeConfigSpec spec, ModConfig.Type type) {

        if (this.isSpecNotBuilt(type)) {

            this.specs.put(type, spec);
            return true;
        }

        return false;
    }

    private ForgeConfigSpec.Builder getBuilder(ModConfig.Type type) {

        return builders.get(type);
    }

    public void createCategory(String name, Consumer<ForgeConfigSpec.Builder> options, ModConfig.Type type, String... comments) {

        this.activeType = type;

        ForgeConfigSpec.Builder builder = builders.get(type);
        if (comments.length != 0) {

            builder.comment(comments);
        }

        builder.push(name);
        options.accept(builder);
        builder.pop();

        this.activeType = null;
    }

    public ModConfig.Type getActiveType() {

        return this.activeType;
    }

}
