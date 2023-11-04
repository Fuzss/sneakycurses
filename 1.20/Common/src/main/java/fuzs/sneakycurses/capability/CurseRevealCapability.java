package fuzs.sneakycurses.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class CurseRevealCapability implements CapabilityComponent {
    private static final String TAG_CURSE_REVEAL_TICKS = "curse_reveal_ticks";
    private static final int DEFAULT_CURSE_REVEAL_TICKS = 1200;

    private int curseRevealTicks = DEFAULT_CURSE_REVEAL_TICKS;

    public CurseRevealCapability(LivingEntity entity) {

    }

    public boolean isAllowedToRevealCurses() {
        if (this.curseRevealTicks == 0) {
            this.curseRevealTicks = DEFAULT_CURSE_REVEAL_TICKS;
            return true;
        }
        return false;
    }

    public void tick() {
        if (this.curseRevealTicks > 0) this.curseRevealTicks--;
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putInt(TAG_CURSE_REVEAL_TICKS, this.curseRevealTicks);
    }

    @Override
    public void read(CompoundTag tag) {
        this.curseRevealTicks = tag.getInt(TAG_CURSE_REVEAL_TICKS);
    }
}
