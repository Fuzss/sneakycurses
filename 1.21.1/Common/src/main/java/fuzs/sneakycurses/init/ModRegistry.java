package fuzs.sneakycurses.init;

import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final BoundTagFactory TAGS = BoundTagFactory.make(SneakyCurses.MOD_ID);
    public static final TagKey<Item> REVEALS_CURSES_ITEM_TAG = TAGS.registerItemTag("reveals_curses");

    public static void touch() {

    }
}
