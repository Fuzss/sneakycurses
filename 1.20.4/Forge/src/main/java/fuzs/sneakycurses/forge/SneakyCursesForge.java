package fuzs.sneakycurses.forge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SneakyCurses.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SneakyCursesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
    }
}
