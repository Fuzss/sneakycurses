package fuzs.sneakycurses.handler;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.config.ServerConfig;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public class CurseRevealHandler {
    private static final String TAG_REVEALED = "Revealed";

    public static boolean tryRevealCurses(ItemStack newStack, RandomSource random) {
        if (random.nextDouble() < SneakyCurses.CONFIG.get(ServerConfig.class).curseRevealChance) {
            CompoundTag itemTag = newStack.getTag();
            if (itemTag == null || itemTag.contains(TAG_REVEALED, Tag.TAG_BYTE)) {
                newStack.addTagElement(TAG_REVEALED, ByteTag.valueOf(true));
                return true;
            }
        }
        return false;
    }

    public static boolean allCursesRevealed(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TAG_REVEALED, Tag.TAG_BYTE) && tag.getBoolean(TAG_REVEALED);
    }
}
