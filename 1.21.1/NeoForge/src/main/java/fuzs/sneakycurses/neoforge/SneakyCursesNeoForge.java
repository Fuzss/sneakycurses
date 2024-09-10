package fuzs.sneakycurses.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.data.ModItemTagsProvider;
import fuzs.sneakycurses.data.client.ModLanguageProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SneakyCurses.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SneakyCursesNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
        DataProviderHelper.registerDataProviders(SneakyCurses.MOD_ID, ModItemTagsProvider::new, ModLanguageProvider::new);
    }
}
