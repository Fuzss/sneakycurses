package com.fuzs.sneakymagic.common;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.ElementRegistry;
import com.fuzs.puzzleslib_sm.element.side.ISidedElement;
import com.fuzs.sneakymagic.SneakyMagic;
import com.fuzs.sneakymagic.client.element.CursesElement;
import com.fuzs.sneakymagic.common.element.CompatibilityElement;
import com.fuzs.sneakymagic.common.element.ExclusivenessElement;
import com.fuzs.sneakymagic.common.element.TweaksElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public class SneakyMagicElements extends ElementRegistry {

    public static final AbstractElement ENCHANTMENT_EXCLUSIVENESS = register("enchantment_exclusiveness", ExclusivenessElement::new);
    public static final AbstractElement ENCHANTMENT_TWEAKS = register("enchantment_tweaks", TweaksElement::new);
    public static final AbstractElement ENCHANTMENT_COMPATIBILITY = register("enchantment_compatibility", CompatibilityElement::new);
    public static final AbstractElement SNEAKY_CURSES = register("sneaky_curses", CursesElement::new, Dist.CLIENT);

    /**
     * create overload so this class and its elements are loaded
     */
    public static void setup(String namespace) {

        ElementRegistry.setup(namespace);
    }

    /**
     * register an element to the namespace of the active mod container
     * @param key identifier for this element
     * @param supplier supplier for element to be registered
     * @return <code>element</code>
     * @param <T> make sure element also extends ISidedElement
     */
    private static <T extends AbstractElement & ISidedElement> AbstractElement register(String key, Supplier<T> supplier) {

        return register(SneakyMagic.MODID, key, supplier, FMLEnvironment.dist);
    }

    /**
     * register an element to the namespace of the active mod container
     * @param key identifier for this element
     * @param supplier supplier for element to be registered
     * @param dist physical side to register on
     * @return <code>element</code>
     * @param <T> make sure element also extends ISidedElement
     */
    private static <T extends AbstractElement & ISidedElement> AbstractElement register(String key, Supplier<T> supplier, Dist dist) {

        return register(SneakyMagic.MODID, key, supplier, dist);
    }

}
