package fuzs.sneakycurses;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingTickCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.AnvilEvents;
import fuzs.puzzleslib.api.network.v3.NetworkHandlerV3;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import fuzs.sneakycurses.init.ModRegistry;
import fuzs.sneakycurses.network.ClientboundTridentItemMessage;
import fuzs.sneakycurses.network.client.ServerboundRequestTridentItemMessage;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SneakyCurses implements ModConstructor {
    public static final String MOD_ID = "sneakycurses";
    public static final String MOD_NAME = "Sneaky Curses";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final NetworkHandlerV3 NETWORK = NetworkHandlerV3.builder(MOD_ID)
            .registerClientbound(ClientboundTridentItemMessage.class)
            .registerServerbound(ServerboundRequestTridentItemMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        AnvilEvents.UPDATE.register(CurseRevealHandler::onAnvilUpdate);
        LivingTickCallback.EVENT.register(CurseRevealHandler::onLivingTick);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
