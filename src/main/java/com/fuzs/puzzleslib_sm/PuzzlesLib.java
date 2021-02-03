package com.fuzs.puzzleslib_sm;

import com.fuzs.puzzleslib_sm.element.registry.ElementRegistry;
import com.fuzs.puzzleslib_sm.registry.RegistryManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
//@Mod(PuzzlesLib.MODID)
public class PuzzlesLib {

    public static final String MODID = "puzzleslib";
    public static final String NAME = "Puzzles Lib";
    public static final Logger LOGGER = LogManager.getLogger(PuzzlesLib.NAME);

    public PuzzlesLib() {

        FMLJavaModLoadingContext.get().getModEventBus().register(RegistryManager.get());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerSetup);
    }

    protected void onCommonSetup(final FMLCommonSetupEvent evt) {

        ElementRegistry.load(evt);
    }

    protected void onClientSetup(final FMLClientSetupEvent evt) {

        ElementRegistry.load(evt);
    }

    protected void onServerSetup(final FMLDedicatedServerSetupEvent evt) {

        ElementRegistry.load(evt);
    }

}
