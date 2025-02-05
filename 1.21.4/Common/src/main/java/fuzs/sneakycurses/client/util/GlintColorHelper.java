package fuzs.sneakycurses.client.util;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.world.item.ItemStack;

public class GlintColorHelper {
    private static ItemStack targetStack = ItemStack.EMPTY;

    public static boolean shouldRenderCursedGlint() {
        return shouldRenderCursedGlint(targetStack);
    }

    public static boolean shouldRenderCursedGlint(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasFoil()) {
            return false;
        } else if (!SneakyCurses.CONFIG.getHolder(ServerConfig.class).isAvailable() ||
                !SneakyCurses.CONFIG.get(ServerConfig.class).cursedItemGlint) {
            return false;
        } else {
            return CurseRevealHandler.anyEnchantIsCursed(itemStack);
        }
    }

    public static void setTargetStack(ItemStack targetStack) {
        GlintColorHelper.targetStack = targetStack;
    }
}
