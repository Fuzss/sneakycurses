package com.fuzs.sneakymagic;

import com.fuzs.sneakymagic.client.handler.CursedTooltipHandler;
import com.fuzs.sneakymagic.common.handler.CompatibilityHandler;
import com.fuzs.sneakymagic.common.CompatibilityManager;
import com.fuzs.sneakymagic.common.handler.EnchantmentHandler;
import com.fuzs.sneakymagic.config.ConfigBuildHandler;
import com.fuzs.sneakymagic.config.ConfigManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(SneakyMagic.MODID)
public class SneakyMagic {

    public static final String MODID = "sneakymagic";
    public static final String NAME = "Sneaky Magic";
    public static final Logger LOGGER = LogManager.getLogger(SneakyMagic.NAME);

    public SneakyMagic() {

        // general setup
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        // config setup
        ConfigBuildHandler.setup();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.Builder.getSpec());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigManager::onModConfig);
        ConfigManager.addListener(new CompatibilityManager()::load);
    }

    private void onCommonSetup(final FMLCommonSetupEvent evt) {

        MinecraftForge.EVENT_BUS.register(new EnchantmentHandler());
        MinecraftForge.EVENT_BUS.register(new CompatibilityHandler());
    }

    private void onClientSetup(final FMLClientSetupEvent evt) {

        MinecraftForge.EVENT_BUS.register(new CursedTooltipHandler());
    }

}
