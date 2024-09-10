package fuzs.sneakycurses.data;

import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.sneakycurses.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;

public class ModItemTagsProvider extends AbstractTagProvider.Items {

    public ModItemTagsProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.REVEALS_CURSES_ITEM_TAG).add(Items.AMETHYST_SHARD);
    }
}
