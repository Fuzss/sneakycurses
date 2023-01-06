package fuzs.sneakycurses.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore {
    @Config(description = "Obfuscate curse enchantments on item tooltips.")
    public boolean obfuscateCurses = true;
    @Config(name = "color_item_name", description = "Cursed items have a red hover name instead of aqua.")
    public boolean colorItemName = false;
    @Config(description = "Turn the enchantment glint red when the item is enchanted with at least one curse.")
    public boolean cursedItemGlint = true;
    @Config(description = "Hide curses from enchanted books if they also hold other enchantments.")
    public boolean affectBooks = false;
    @Config(name = "shift_shows_curses", description = "Temporarily show tooltip as usual including curses while shift key is held.")
    public boolean shiftShows = false;
}
