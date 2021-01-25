package com.fuzs.sneakymagic;

import com.fuzs.puzzleslib_sm.PuzzlesLib;
import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.sneakymagic.common.SneakyMagicElements;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(SneakyMagic.MODID)
public class SneakyMagic extends PuzzlesLib {

    public static final String MODID = "sneakymagic";
    public static final String NAME = "Sneaky Magic";
    public static final Logger LOGGER = LogManager.getLogger(SneakyMagic.NAME);

    public SneakyMagic() {

        super();
        SneakyMagicElements.setup(MODID);
        ConfigManager.get().load();
//        ConfigManager.get().addListener(new CompatibilityManager()::load);
    }

}
