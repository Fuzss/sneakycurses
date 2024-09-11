package fuzs.sneakycurses.neoforge.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.SneakyCursesClient;
import fuzs.sneakycurses.data.client.ModLanguageProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SneakyCurses.MOD_ID, dist = Dist.CLIENT)
public class SneakyCursesNeoForgeClient {

    public SneakyCursesNeoForgeClient() {
        ClientModConstructor.construct(SneakyCurses.MOD_ID, SneakyCursesClient::new);
        DataProviderHelper.registerDataProviders(SneakyCurses.MOD_ID, ModLanguageProvider::new);
    }
}
