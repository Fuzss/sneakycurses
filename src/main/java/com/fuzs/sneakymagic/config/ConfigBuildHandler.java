package com.fuzs.sneakymagic.config;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("WeakerAccess")
public class ConfigBuildHandler {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ForgeConfigSpec.BooleanValue SELECTIVE_MENDING;

    static {

        BUILDER.push("general");
        TRUE_INFINITY = ConfigBuildHandler.BUILDER.comment("Infinity enchantment no longer requires a single arrow to be present in the player inventory.").define("True Infinity", true);
        SELECTIVE_MENDING = ConfigBuildHandler.BUILDER.comment("Mending only selects items for repair that actually need it.").define("Selective Mending", true);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}