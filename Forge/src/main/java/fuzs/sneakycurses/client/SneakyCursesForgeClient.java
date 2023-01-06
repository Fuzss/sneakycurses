package fuzs.sneakycurses.client;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = SneakyCurses.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SneakyCursesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ItemTooltipEvent evt) -> {
            ItemTooltipHandler.onItemTooltip(evt.getItemStack(), evt.getFlags(), evt.getToolTip());
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.Init.Post evt) -> {
            ItemTooltipHandler.setRandomSeed();
        });
    }
}
