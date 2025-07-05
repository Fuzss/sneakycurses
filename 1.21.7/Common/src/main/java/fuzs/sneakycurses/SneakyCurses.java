package fuzs.sneakycurses;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.CreateAnvilResultCallback;
import fuzs.sneakycurses.config.ClientConfig;
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

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        CreateAnvilResultCallback.EVENT.register(CurseRevealHandler::onCreateAnvilResult);
        EntityTickEvents.END.register(CurseRevealHandler::onEndEntityTick);
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToClient(ClientboundTridentItemMessage.class, ClientboundTridentItemMessage.STREAM_CODEC);
        context.playToServer(ServerboundRequestTridentItemMessage.class,
                ServerboundRequestTridentItemMessage.STREAM_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
