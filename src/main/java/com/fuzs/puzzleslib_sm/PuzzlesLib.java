package com.fuzs.puzzleslib_sm;

import com.fuzs.puzzleslib_sm.capability.CapabilityController;
import com.fuzs.puzzleslib_sm.element.registry.ElementRegistry;
import com.fuzs.puzzleslib_sm.network.NetworkHandler;
import com.fuzs.puzzleslib_sm.registry.RegistryManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
//@Mod(PuzzlesLib.MODID)
public class PuzzlesLib {

    public static final String MODID = "puzzleslib";
    public static final String NAME = "Puzzles Lib";
    public static final Logger LOGGER = LogManager.getLogger(PuzzlesLib.NAME);

    private static RegistryManager registryManager;
    private static NetworkHandler networkHandler;
    private static CapabilityController capabilityController;

    public PuzzlesLib() {

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

    public static RegistryManager getRegistryManager() {

        return getOrElse(registryManager, RegistryManager::new, instance -> registryManager = instance);
    }

    public static NetworkHandler getNetworkHandler() {

        return getOrElse(networkHandler, NetworkHandler::new, instance -> networkHandler = instance);
    }

    public static CapabilityController getCapabilityController() {

        return getOrElse(capabilityController, CapabilityController::new, instance -> capabilityController = instance);
    }

    private static <T> T getOrElse(@Nullable T instance, Supplier<T> supplier, Consumer<T> consumer) {

        if (instance == null) {

            instance = supplier.get();
            consumer.accept(instance);
        }

        return instance;
    }

}
