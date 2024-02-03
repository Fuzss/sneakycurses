package fuzs.sneakycurses.fabric;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.sneakycurses.SneakyCurses;
import net.fabricmc.api.ModInitializer;

public class SneakyCursesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
    }
}
