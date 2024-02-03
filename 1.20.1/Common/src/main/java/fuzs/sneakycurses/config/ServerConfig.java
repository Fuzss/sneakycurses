package fuzs.sneakycurses.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(name = "obfuscate_curses_on_tooltips", description = "Obfuscate curse enchantments with enchantment runes on item tooltips.")
    public boolean obfuscateCurses = true;
    @Config(name = "tint_enchantment_glint_when_cursed", description = "Tint the enchantment glint in a red shade when the item is enchanted with curses.")
    public boolean cursedItemGlint = true;
    @Config(description = "Obfuscate curses on enchanted books.")
    public boolean affectBooks = true;
    @Config(name = "shift_shows_curses_in_creative", description = "Temporarily show curses as normal text while any shift key is held in creative mode.")
    public boolean shiftShows = true;
    @Config(name = "material_cost_for_revealing_curses_in_anvil", description = "Amount of experience levels required to make curses on an item visible by combining with an item from the 'sneakycurses:reveal_curses' tag (amethyst shards by default) in an anvil.")
    @Config.IntRange(min = 1)
    public int revealCursesCost = 5;
    @Config(description = "Chance wearing or using a cursed piece of equipment will trigger the curses to be revealed. Set to 0.0 to disable revealing curses this way.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double curseRevealChance = 0.05;
}
