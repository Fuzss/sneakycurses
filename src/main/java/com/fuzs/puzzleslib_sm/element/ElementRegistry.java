package com.fuzs.puzzleslib_sm.element;

import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.element.side.IClientElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.puzzleslib_sm.element.side.IServerElement;
import com.fuzs.puzzleslib_sm.element.side.ISidedElement;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * default registry for elements
 */
@SuppressWarnings("unused")
public abstract class ElementRegistry {

    /**
     * storage for elements of all mods for performing actions on all of them
     */
    private static final BiMap<ResourceLocation, AbstractElement> ELEMENTS = HashBiMap.create();

    /**
     * register an element, overload this to set mod namespace
     * every element must be sided, meaning must somehow implement {@link ISidedElement}
     * @param namespace namespace of registering mod
     * @param key identifier for this element
     * @param supplier supplier for element to be registered
     * @param dist physical side to register on
     * @return <code>element</code>
     * @param <T> make sure element also extends ISidedElement
     */
    @Nullable
    protected static <T extends AbstractElement & ISidedElement> AbstractElement register(String namespace, String key, Supplier<T> supplier, Dist dist) {

        if (dist == FMLEnvironment.dist) {

            AbstractElement element = supplier.get();
            ELEMENTS.put(new ResourceLocation(namespace, key), element);
            return element;
        }

        return null;
    }

    /**
     * @param element element to get name for
     * @return name set in elements registry
     */
    public static ResourceLocation getRegistryName(AbstractElement element) {

        return ELEMENTS.inverse().get(element);
    }

    /**
     * get an element from another mod which uses this registry
     * @param namespace namespace of owning mod
     * @param key key for element to get
     * @return optional element
     */
    public static Optional<AbstractElement> get(String namespace, String key) {

        return Optional.ofNullable(ELEMENTS.get(new ResourceLocation(namespace, key)));
    }

    /**
     * to be used by other mods using this library
     * @param namespace namespace of owning mod
     * @param key key for element to get
     * @param path path for config value
     * @return the config value
     */
    @SuppressWarnings("OptionalIsPresent")
    public static Optional<Object> getConfigValue(String namespace, String key, String... path) {

        Optional<AbstractElement> element = get(namespace, key);
        if (element.isPresent()) {

            return getConfigValue(element.get(), path);
        }

        return Optional.empty();
    }

    /**
     * to be used from inside of this mod
     * @param element element to get value from
     * @param path path for config value
     * @return the config value
     */
    public static Optional<Object> getConfigValue(AbstractElement element, String... path) {

        if (element.isEnabled()) {

            assert path.length != 0 : "Unable to get config value: " + "Invalid config path";

            String fullPath = Stream.concat(Stream.of(getRegistryName(element).getPath()), Stream.of(path)).collect(Collectors.joining("."));
            return Optional.of(ConfigManager.get().getValueFromPath(fullPath));
        }

        return Optional.empty();
    }

    /**
     * cast an element to its class type to make unique methods accessible
     * @param element element to get
     * @param <T> return type
     * @return <code>element</code> cast as <code>T</code>
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractElement> T getAs(AbstractElement element) {

        return (T) element;
    }

    /**
     * generate general config section for controlling elements, setup individual config sections and collect events to be registered in {@link #load}
     */
    protected static void setup(String namespace) {

        Set<AbstractElement> elements = getOwnElements(namespace);

        assert !elements.isEmpty() : "Unable to setup elements for " + namespace + ": " + "No elements registered";

        setupGeneralSide(elements, element -> element instanceof ICommonElement, ModConfig.Type.COMMON, FMLEnvironment.dist);
        setupGeneralSide(elements, element -> element instanceof IClientElement && !(element instanceof ICommonElement), ModConfig.Type.CLIENT, Dist.CLIENT);
        setupGeneralSide(elements, element -> element instanceof IServerElement && !(element instanceof ICommonElement), ModConfig.Type.SERVER, Dist.DEDICATED_SERVER);

        elements.forEach(AbstractElement::setup);
    }

    /**
     * setup general section with control over individual elements for all config types
     * @param elements all elements for this mod
     * @param isCurrentSide instanceof check for {@link ISidedElement}
     * @param type config type to create general category for
     * @param dist physical side this element can only be registered on
     */
    private static void setupGeneralSide(Set<AbstractElement> elements, Predicate<AbstractElement> isCurrentSide, ModConfig.Type type, Dist dist) {

        Set<AbstractElement> sideElements = elements.stream().filter(isCurrentSide).collect(Collectors.toSet());
        if (!sideElements.isEmpty()) {

            assert dist == FMLEnvironment.dist : "Unable to setup element: " + "Sided element registered on common side";

            ConfigManager.builder().create("general", builder -> sideElements.forEach(element -> element.setupGeneralConfig(builder)), type);
        }
    }

    /**
     * @return elements for active mod container as set
     */
    private static Set<AbstractElement> getOwnElements(String namespace) {

        return ELEMENTS.entrySet().stream()
                .filter(entry -> entry.getKey().getNamespace().equals(namespace))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    /**
     * execute on common load and register events
     * loads all elements, no matter which mod they're from
     */
    public static void load() {

        ELEMENTS.values().forEach(AbstractElement::load);
        ELEMENTS.values().stream()
                .filter(element -> element instanceof ICommonElement)
                .map(element -> (ICommonElement) element)
                .forEach(ICommonElement::loadCommon);
    }

    /**
     * execute on client load
     * loads all elements, no matter which mod they're from
     */
    public static void loadClient() {

        ELEMENTS.values().stream()
                .filter(element -> element instanceof IClientElement)
                .map(element -> (IClientElement) element)
                .forEach(IClientElement::loadClient);
    }

    /**
     * execute on server load
     * loads all elements, no matter which mod they're from
     */
    public static void loadServer() {

        ELEMENTS.values().stream()
                .filter(element -> element instanceof IServerElement)
                .map(element -> (IServerElement) element)
                .forEach(IServerElement::loadServer);
    }

}
