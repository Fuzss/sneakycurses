package fuzs.sneakycurses;

import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class SneakyCursesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(SneakyCurses.MOD_ID).accept(new SneakyCurses());
    }
}
