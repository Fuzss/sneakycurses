package com.fuzs.puzzleslib_sm.element;

import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.config.deserialize.EntryCollectionBuilder;
import com.fuzs.puzzleslib_sm.element.side.IClientElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.puzzleslib_sm.element.side.IServerElement;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * all features a mod adds are structured into elements which are then registered, this is an abstract version
 */
@SuppressWarnings("unused")
public abstract class AbstractElement extends EventListener implements IConfigurableElement {

    /**
     * all events registered by this element
     */
    private final List<EventStorage<? extends Event>> events = Lists.newArrayList();
    /**
     * is this element enabled (are events registered)
     */
    private boolean enabled;

    /**
     * @return name of this set in elements registry
     */
    private String getRegistryName() {

        return ElementRegistry.getRegistryName(this).getPath();
    }

    @Override
    public final String getDisplayName() {

        return Stream.of(this.getRegistryName().split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    @Override
    public boolean getDefaultState() {

        return true;
    }

    @Override
    public final void setupGeneralConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment(this.getDescription()).define(this.getDisplayName(), this.getDefaultState()), this::setEnabled);
    }

    @Override
    public void unload() {

    }

    /**
     * build element config and get event listeners
     */
    public final void setup() {

        this.setupConfig(this.getRegistryName());
        this.setupEvents();
    }

    /**
     * setup config for all sides
     * @param elementId id of this element for config section
     */
    private void setupConfig(String elementId) {

        Consumer<ICommonElement> commonConfig = element -> ConfigManager.builder().create(elementId, element::setupCommonConfig, ModConfig.Type.COMMON, element.getCommonDescription());
        Consumer<IClientElement> clientConfig = element -> ConfigManager.builder().create(elementId, element::setupClientConfig, ModConfig.Type.CLIENT, element.getClientDescription());
        Consumer<IServerElement> serverConfig = element -> ConfigManager.builder().create(elementId, element::setupServerConfig, ModConfig.Type.SERVER, element.getServerDescription());
        this.setupAllSides(commonConfig, clientConfig, serverConfig);
    }

    /**
     * setup events for all sides
     */
    private void setupEvents() {

        this.setupAllSides(ICommonElement::setupCommon, IClientElement::setupClient, IServerElement::setupServer);
    }

    /**
     * @param commonSetup consumer if implements {@link ICommonElement}
     * @param clientSetup consumer if implements {@link IClientElement}
     * @param serverSetup consumer if implements {@link IServerElement}
     */
    private void setupAllSides(Consumer<ICommonElement> commonSetup, Consumer<IClientElement> clientSetup, Consumer<IServerElement> serverSetup) {

        if (this instanceof ICommonElement) {

            commonSetup.accept(((ICommonElement) this));
        }

        if (FMLEnvironment.dist.isClient() && this instanceof IClientElement) {

            clientSetup.accept(((IClientElement) this));
        }

        if (FMLEnvironment.dist.isDedicatedServer() && this instanceof IServerElement) {

            serverSetup.accept(((IServerElement) this));
        }
    }

    /**
     * register Forge events from internal storage and call sided load methods
     * no need to check physical side as the setup event won't be called anyways
     */
    public final void load(ParallelDispatchEvent evt) {

        this.loadEvents(evt);
        if (evt instanceof FMLCommonSetupEvent && this instanceof ICommonElement) {

            ((ICommonElement) this).loadCommon();
        } else if (evt instanceof FMLClientSetupEvent && this instanceof IClientElement) {

            ((IClientElement) this).loadClient();
        } else if (evt instanceof FMLDedicatedServerSetupEvent && this instanceof IServerElement) {

            ((IServerElement) this).loadServer();
        }
    }

    /**
     * initial registering for events
     * @param evt setup event this is called from
     */
    private void loadEvents(ParallelDispatchEvent evt) {

        if (this instanceof ICommonElement) {

            if (evt instanceof FMLCommonSetupEvent) {

                this.reload(true);
            }
        } else if (evt instanceof FMLClientSetupEvent || evt instanceof FMLDedicatedServerSetupEvent) {

            this.reload(true);
        }
    }

    /**
     * update status of all stored events
     * @param isInit is this method called during initial setup
     */
    private void reload(boolean isInit) {

        if (this.isEnabled() || this.isAlwaysEnabled()) {

            this.getEvents().forEach(EventStorage::register);
        } else if (!isInit) {

            // nothing to unregister during initial setup
            this.getEvents().forEach(EventStorage::unregister);
            this.unload();
        }
    }

    @Override
    public final boolean isEnabled() {

        return this.enabled;
    }

    /**
     * are the events from this mod always active
     * @return is always enabled
     */
    public boolean isAlwaysEnabled() {

        return false;
    }

    /**
     * set {@link #enabled} state, reload when changed
     * @param enabled enabled
     */
    private void setEnabled(boolean enabled) {

        if (enabled != this.enabled) {

            this.enabled = enabled;
            this.reload(false);
        }
    }

    @Override
    public final List<EventStorage<? extends Event>> getEvents() {

        return this.events;
    }

    /**
     * @param entry config entry to add
     * @param action consumer for updating value when changed
     * @param <S> type of config value
     * @param <T> field type
     */
    public static <S extends ForgeConfigSpec.ConfigValue<T>, T> void addToConfig(S entry, Consumer<T> action) {

        ConfigManager.get().registerEntry(entry, action);
    }

    /**
     * @param entry config entry to add
     * @param action consumer for updating value when changed
     * @param transformer transformation to apply when returning value
     * @param <S> type of config value
     * @param <T> field type
     * @param <R> final return type of config entry
     */
    public static <S extends ForgeConfigSpec.ConfigValue<T>, T, R> void addToConfig(S entry, Consumer<R> action, Function<T, R> transformer) {

        ConfigManager.get().registerEntry(entry, action, transformer);
    }

    /**
     * deserialize string <code>data</code> into entries of a <code>registry</code>
     * @param data data as string list as provided by Forge config
     * @param registry registry to get entries from
     * @param <T> type of registry
     * @return deserialized data as set
     */
    public static <T extends IForgeRegistryEntry<T>> Set<T> deserializeToSet(List<String> data, IForgeRegistry<T> registry) {

        return new EntryCollectionBuilder<>(registry).buildEntrySet(data);
    }

    /**
     * deserialize string <code>data</code> into entries of a <code>registry</code>
     * @param data data as string list as provided by Forge config
     * @param registry registry to get entries from
     * @param <T> type of registry
     * @return deserialized data as map
     */
    public static <T extends IForgeRegistryEntry<T>> Map<T, double[]> deserializeToMap(List<String> data, IForgeRegistry<T> registry) {

        return new EntryCollectionBuilder<>(registry).buildEntryMap(data);
    }

}
