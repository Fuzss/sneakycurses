package fuzs.sneakycurses.client.util;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.world.item.ItemStack;

public class GlintColorHelper {
    private static ItemStack targetStack = ItemStack.EMPTY;

    public static boolean shouldRenderCursedGlint() {
        if (!SneakyCurses.CONFIG.get(ServerConfig.class).cursedItemGlint) return false;
        return CurseRevealHandler.anyEnchantIsCursed(targetStack);
    }

    public static void setTargetStack(ItemStack targetStack) {
        GlintColorHelper.targetStack = targetStack;
    }
}
