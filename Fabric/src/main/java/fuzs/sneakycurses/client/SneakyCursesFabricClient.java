package fuzs.sneakycurses.client;

import fuzs.sneakycurses.client.handler.ItemTooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public class SneakyCursesFabricClient implements ClientModInitializer {
    public static ItemStack targetStack = ItemStack.EMPTY;

    private static void registerHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ScreenEvents.AFTER_INIT.register((Minecraft client, Screen screen, int scaledWidth, int scaledHeight) -> {
            ItemTooltipHandler.setRandomSeed();
        });
    }

    @Override
    public void onInitializeClient() {
        registerHandlers();
    }
}
