package fuzs.sneakycurses.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import net.minecraft.world.item.DyeColor;

public class ClientConfig implements ConfigCore {
    @Config(description = "The glint color for items enchanted with curses.")
    public DyeColor cursedGlintColor = DyeColor.RED;
}
