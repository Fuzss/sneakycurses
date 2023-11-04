package fuzs.sneakycurses;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.data.v2.core.DataProviderHelper;
import fuzs.sneakycurses.data.ModItemTagsProvider;
import fuzs.sneakycurses.data.client.ModLanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SneakyCurses.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SneakyCursesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
        DataProviderHelper.registerDataProviders(SneakyCurses.MOD_ID, ModItemTagsProvider::new, ModLanguageProvider::new);
    }
}
