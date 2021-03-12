package com.fuzs.sneakymagic;

import com.fuzs.puzzleslib_sm.PuzzlesLib;
import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.registry.ElementRegistry;
import com.fuzs.sneakymagic.client.element.SneakyCursesElement;
import com.fuzs.sneakymagic.element.CompatibilityElement;
import com.fuzs.sneakymagic.element.ExclusivenessElement;
import com.fuzs.sneakymagic.element.ImprovementsElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(SneakyMagic.MODID)
public class SneakyMagic extends PuzzlesLib {

    public static final String MODID = "sneakymagic";
    public static final String NAME = "Sneaky Magic";
    public static final Logger LOGGER = LogManager.getLogger(SneakyMagic.NAME);

    public static final AbstractElement ENCHANTMENT_EXCLUSIVENESS = register("enchantment_exclusiveness", ExclusivenessElement::new);
    public static final AbstractElement ENCHANTMENT_IMPROVEMENTS = register("enchantment_improvements", ImprovementsElement::new);
    public static final AbstractElement ENCHANTMENT_COMPATIBILITY = register("enchantment_compatibility", CompatibilityElement::new);
    public static final AbstractElement SNEAKY_CURSES = register("sneaky_curses", SneakyCursesElement::new, Dist.CLIENT);

    public SneakyMagic() {

        super();
        ElementRegistry.setup(MODID);
        ConfigManager.get().load();
    }

}
