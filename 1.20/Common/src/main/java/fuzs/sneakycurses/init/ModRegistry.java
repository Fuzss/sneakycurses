package fuzs.sneakycurses.init;

import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.capability.CurseRevealCapability;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final BoundTagFactory TAGS = BoundTagFactory.make(SneakyCurses.MOD_ID);
    public static final TagKey<Item> REVEALS_CURSES_ITEM_TAG = TAGS.registerItemTag("reveals_curses");

    static final CapabilityController CAPABILITIES = CapabilityController.from(SneakyCurses.MOD_ID);
    public static final CapabilityKey<CurseRevealCapability> CURSE_REVEAL_CAPABILITY = CAPABILITIES.registerEntityCapability("curse_reveal", CurseRevealCapability.class, CurseRevealCapability::new, LivingEntity.class);

    public static void touch() {

    }
}
