package fuzs.sneakycurses.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Chance an action with a cursed item (e.g. equipping or using) will trigger the curses to be revealed. Set to 0.0 to disable revealing curses.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double curseRevealChance = 0.015;
}
