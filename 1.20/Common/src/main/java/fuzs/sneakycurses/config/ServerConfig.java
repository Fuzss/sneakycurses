package fuzs.sneakycurses.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Obfuscate curse enchantments with runes on item tooltips.")
    public boolean obfuscateCurses = true;
    @Config(description = "Turn the enchantment glint red when the item is enchanted with at least one curse.")
    public boolean cursedItemGlint = true;
    @Config(description = "Hide curses from enchanted books.")
    public boolean affectBooks = true;
    @Config(name = "shift_shows_curses", description = "Temporarily show curses as normal text while any shift key is held in creative mode.")
    public boolean shiftShows = true;
    @Config(description = "Amount of experience levels required to make curses on an item visible by combining with an item from the 'sneakycurses:reveal_curses' tag (amethyst shards by default) in an anvil.")
    @Config.IntRange(min = 1)
    public int revealCursesCost = 5;
    @Config(description = "Chance wearing or using a cursed piece of equipment will trigger the curses to be revealed. Set to 0.0 to disable revealing curses this way.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double curseRevealChance = 0.05;
}
