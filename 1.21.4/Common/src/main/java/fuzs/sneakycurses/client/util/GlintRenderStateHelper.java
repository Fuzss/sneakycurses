package fuzs.sneakycurses.client.util;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.world.item.ItemStack;

public class GlintRenderStateHelper {
    private static boolean glintRenderState;

    public static boolean getRenderState() {
        return glintRenderState;
    }

    public static void extractRenderState(ItemStack itemStack) {
        glintRenderState = isItemStackCursed(itemStack);
    }

    public static boolean isItemStackCursed(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasFoil()) {
            return false;
        } else if (!SneakyCurses.CONFIG.getHolder(ServerConfig.class).isAvailable() ||
                !SneakyCurses.CONFIG.get(ServerConfig.class).cursedItemGlint) {
            return false;
        } else {
            return CurseRevealHandler.anyEnchantIsCursed(itemStack);
        }
    }

    public static void copyRenderState(GlintItemStackRenderState renderState) {
        glintRenderState = renderState.sneakycurses$getGlint();
    }

    public static void clearRenderState() {
        glintRenderState = false;
    }
}
