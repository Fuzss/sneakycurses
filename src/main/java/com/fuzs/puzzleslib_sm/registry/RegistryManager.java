package com.fuzs.puzzleslib_sm.registry;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class RegistryManager {
    
    private static RegistryManager instance;

    private final Map<String, RegistryData> registryData = new HashMap<>();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRegisterEntry(RegistryEvent.Register<?> evt) {

        this.registryData.values().forEach(data -> data.register(evt.getRegistry()));
    }

    public void register(String path, IForgeRegistryEntry<?> entry) {

        if (entry == null) {

            throw new IllegalArgumentException("Can't register null object.");
        }

        entry.setRegistryName(new ResourceLocation(this.getActiveNamespace(), path));
        this.getActiveData().put(entry.getRegistryType(), entry);
    }

    /**
     * get active modid so entries can still be associated with the mod
     * @return active modid
     */
    private String getActiveNamespace() {

        return ModLoadingContext.get().getActiveNamespace();
    }

    private RegistryData getActiveData() {

        return this.getRegistryData(this.getActiveNamespace());
    }

    private RegistryData getRegistryData(String modid) {

        RegistryData data = this.registryData.get(modid);
        if (data == null) {

            data = new RegistryData();
            this.registryData.put(modid, data);
        }

        return data;
    }

    public static RegistryManager get() {

        if (instance == null) {

            instance = new RegistryManager();
        }

        return instance;
    }
    
    static class RegistryData {

        private final ArrayListMultimap<Class<?>, IForgeRegistryEntry<?>> defers = ArrayListMultimap.create();

        @SuppressWarnings("unchecked")
        <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry) {

            Class<T> type = registry.getRegistrySuperType();
            if (this.defers.containsKey(type)) {

                for (IForgeRegistryEntry<?> entry : this.defers.get(type)) {

                    registry.register((T) entry);
                }

                this.defers.removeAll(type);
            }
        }

        void put(Class<?> clazz, IForgeRegistryEntry<?> entry) {

            this.defers.put(clazz, entry);
        }

    }

}
