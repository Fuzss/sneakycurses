package com.fuzs.puzzleslib_sm.registry;

import com.fuzs.puzzleslib_sm.util.INamespaceLocator;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * handles registering to forge registries
 */
public class RegistryManager implements INamespaceLocator {

    /**
     * internal storage for collecting and registering registry entries
     */
    private final ArrayListMultimap<Class<?>, IForgeRegistryEntry<?>> registryEntries = ArrayListMultimap.create();

    /**
     * add listener for {@link RegistryEvent}
     */
    public RegistryManager() {

        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRegistryRegister(RegistryEvent.Register<?> evt) {

        this.addAllToRegistry(evt.getRegistry());
    }

    /**
     * add all entries for registry type to registry
     * @param registry active registry
     * @param <T> type of registry entry
     */
    @SuppressWarnings("unchecked")
    private <T extends IForgeRegistryEntry<T>> void addAllToRegistry(IForgeRegistry<T> registry) {

        Class<T> type = registry.getRegistrySuperType();
        if (this.registryEntries.containsKey(type)) {

            for (IForgeRegistryEntry<?> entry : this.registryEntries.get(type)) {

                registry.register((T) entry);
            }

            this.registryEntries.removeAll(type);
        }
    }

    /**
     * register any type of registry entry with a path
     * @param path path for new entry
     * @param entry entry to register
     */
    public void register(String path, IForgeRegistryEntry<?> entry) {

        if (entry == null) {

            throw new IllegalArgumentException("Can't register null object.");
        }

        entry.setRegistryName(new ResourceLocation(this.getActiveNamespace(), path));
        this.registryEntries.put(entry.getRegistryType(), entry);
    }

}
