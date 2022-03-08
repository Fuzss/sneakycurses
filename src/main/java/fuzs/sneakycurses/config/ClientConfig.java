package fuzs.sneakycurses.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig extends AbstractConfig {
    @Config(description = "Hide curse enchantments from the item tooltip.")
    public boolean hideCurses = true;
    @Config(name = "color_item_name", description = "Cursed items have a red hover name instead of aqua.")
    public boolean colorName = true;
    @Config(description = "Hide enchantment glint when the item is solely enchanted with curses.")
    public boolean disguiseItem = true;
    @Config(description = "Hide curses from enchanted books if they also hold other enchantments.")
    public boolean affectBooks = false;
    @Config(name = "shift_shows_curses", description = "Temporarily show tooltip as usual including curses while shift key is held.")
    public boolean shiftShows = false;

    public ClientConfig() {
        super("");
    }
}
