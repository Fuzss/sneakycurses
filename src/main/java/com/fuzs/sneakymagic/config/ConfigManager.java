package com.fuzs.sneakymagic.config;

import com.fuzs.sneakymagic.SneakyMagic;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ConfigManager {

    /**
     * singleton instance
     */
    private static ConfigManager instance;

    /**
     * all config entries as a set
     */
    private final Set<ConfigEntry<? extends ForgeConfigSpec.ConfigValue<?>, ?>> configEntries = Sets.newHashSet();
    /**
     * listeners to call when a config is somehow loaded
     */
    private final Map<Runnable, ConfigLoadState> configListeners = Maps.newHashMap();

    /**
     * this class is a singleton
     */
    private ConfigManager() {

    }

    /**
     * fires on both "loading" and "reloading", "loading" phase is required for initial setup
     * @param evt event provided by Forge
     */
    public void onModConfig(final ModConfig.ModConfigEvent evt) {

        ModConfig.Type type = evt.getConfig().getType();
        if (ConfigBuilder.isSpecNotBuilt(type) || ConfigBuilder.isSpecNotLoaded(type)) {

            SneakyMagic.LOGGER.error("Unable to get values from config: " + "Config spec not present");
        } else {

            // no need to check modid or anything as this is only fired for the mod
            this.syncType(type);
            this.notifyListeners(ConfigLoadState.getState(evt));
        }
    }

    /**
     * sync all config entries no matter which type
     */
    public void sync() {

        this.configEntries.forEach(ConfigEntry::sync);
    }

    /**
     * sync config entries for specific type of config
     * @param type type of config to sync
     */
    private void syncType(ModConfig.Type type) {

        this.configEntries.stream().filter(configValue -> configValue.getType() == type).forEach(ConfigEntry::sync);
    }

    /**
     * register config entry on both client and server
     * @param entry source config value object
     * @param action action to perform when value changes (is reloaded)
     * @param <S> config value of a certain type
     * @param <T> type for value
     */
    public <S extends ForgeConfigSpec.ConfigValue<T>, T> void registerCommonEntry(S entry, Consumer<T> action) {

        this.registerEntry(ModConfig.Type.COMMON, entry, action);
    }

    /**
     * register config entry on the client
     * @param entry source config value object
     * @param action action to perform when value changes (is reloaded)
     * @param <S> config value of a certain type
     * @param <T> type for value
     */
    public <S extends ForgeConfigSpec.ConfigValue<T>, T> void registerClientEntry(S entry, Consumer<T> action) {

        this.registerEntry(ModConfig.Type.CLIENT, entry, action);
    }

    /**
     * register config entry on the server
     * @param entry source config value object
     * @param action action to perform when value changes (is reloaded)
     * @param <S> config value of a certain type
     * @param <T> type for value
     */
    public <S extends ForgeConfigSpec.ConfigValue<T>, T> void registerServerEntry(S entry, Consumer<T> action) {

        this.registerEntry(ModConfig.Type.SERVER, entry, action);
    }

    /**
     * register config entry for given type
     * @param type type of config to register for
     * @param entry source config value object
     * @param action action to perform when value changes (is reloaded)
     * @param <S> config value of a certain type
     * @param <T> type for value
     */
    private <S extends ForgeConfigSpec.ConfigValue<T>, T> void registerEntry(ModConfig.Type type, S entry, Consumer<T> action) {

        this.configEntries.add(new ConfigEntry<>(type, entry, action));
    }

    /**
     * register config entry for active type
     * @param <S> config value of a certain type
     * @param <T> type for value
     * @param entry source config value object
     * @param action action to perform when value changes (is reloaded)
     */
    public <S extends ForgeConfigSpec.ConfigValue<T>, T> void registerEntry(S entry, Consumer<T> action) {

        if (ConfigBuilder.getActiveType() == null) {

            SneakyMagic.LOGGER.error("Unable to register config entry: " + "Active builder is null");
        } else if (ConfigBuilder.isSpecNotBuilt(ConfigBuilder.getActiveType())) {

            this.configEntries.add(new ConfigEntry<>(ConfigBuilder.getActiveType(), entry, action));
        } else {

            SneakyMagic.LOGGER.error("Unable to register config entry: " + "Config spec already built");
        }
    }

    /**
     * add a listener for when the config is loaded and reloaded
     * @param listener listener to add
     */
    public void addListener(Runnable listener) {

        this.addListener(listener, ConfigLoadState.BOTH);
    }

    /**
     * add a listener for when the config is loaded
     * @param listener listener to add
     */
    public void addLoadingListener(Runnable listener) {

        this.addListener(listener, ConfigLoadState.LOADING);
    }

    /**
     * add a listener for when the config is reloaded
     * @param listener listener to add
     */
    public void addReloadingListener(Runnable listener) {

        this.addListener(listener, ConfigLoadState.RELOADING);
    }

    /**
     * add a listener for when the config is somehow loaded
     * @param listener listener to add
     * @param state load states when to call this listener
     */
    private void addListener(Runnable listener, ConfigLoadState state) {

        this.configListeners.merge(listener, state, (state1, state2) -> state1 != state2 ? ConfigLoadState.BOTH : state1);
    }

    /**
     * call listeners for state as the config has somehow been loaded
     * @param state config load state
     */
    private void notifyListeners(ConfigLoadState state) {

        this.configListeners.entrySet().stream().filter(entry -> entry.getValue().matches(state)).map(Map.Entry::getKey).forEach(Runnable::run);
    }

    /**
     * @param type type of config
     * @param modId modid this config belongs to
     * @return config name as if it were generated by Forge itself
     */
    public static String getConfigName(ModConfig.Type type, String modId) {

        return String.format("%s-%s.toml", modId, type.extension());
    }

    /**
     * put config into it's own folder when there are multiples
     * @param type type of config
     * @param modId modid this config belongs to
     * @return name lead by folder
     */
    public static String getConfigNameInFolder(ModConfig.Type type, String modId) {

        return modId + File.separator + getConfigName(type, modId);
    }

    /**
     * @return instance of this
     */
    public static ConfigManager get() {

        if (instance == null) {

            instance = new ConfigManager();
        }

        return instance;
    }

    /**
     * internal storage for registered config entries
     * @param <S> config value of a certain type
     * @param <T> type for value
     */
    private static class ConfigEntry<S extends ForgeConfigSpec.ConfigValue<T>, T> {

        /**
         * config type of this entry
         */
        final ModConfig.Type type;
        /**
         * config value entry
         */
        final S entry;
        /**
         * action to perform when the entry is updated
         */
        final Consumer<T> action;

        /**
         * new entry storage
         */
        ConfigEntry(ModConfig.Type type, S entry, Consumer<T> action) {

            this.type = type;
            this.entry = entry;
            this.action = action;
        }

        /**
         * get type for filtering purposes
         * @return type of this
         */
        ModConfig.Type getType() {

            return this.type;
        }

        /**
         * get value from config value and supply it to consumer
         */
        void sync() {

            this.action.accept(this.entry.get());
        }

    }

    /**
     * state for when to trigger listeners
     */
    private enum ConfigLoadState {

        LOADING, RELOADING, BOTH;

        /**
         * check if two states are compatible
         * @param state state to match with
         * @return are states compatible
         */
        boolean matches(ConfigLoadState state) {

            if (state == BOTH || this == BOTH) {

                return true;
            } else if (state == LOADING && this != RELOADING) {

                return true;
            }

            return state == RELOADING && this != LOADING;
        }

        /**
         * get state for an event object
         * @param evt event to get state for
         * @return state for event
         */
        static ConfigLoadState getState(ModConfig.ModConfigEvent evt) {

            return evt instanceof ModConfig.Loading ? LOADING : RELOADING;
        }

    }

}
