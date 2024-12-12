package fuzs.sneakycurses.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.data.ModItemTagsProvider;
import net.neoforged.fml.common.Mod;

@Mod(SneakyCurses.MOD_ID)
public class SneakyCursesNeoForge {

    public SneakyCursesNeoForge() {
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
        DataProviderHelper.registerDataProviders(SneakyCurses.MOD_ID, ModItemTagsProvider::new);
    }
}
