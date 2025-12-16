package fuzs.sneakycurses.init;

import com.mojang.serialization.Codec;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(SneakyCurses.MOD_ID);
    public static final Holder.Reference<DataComponentType<Unit>> REVEAL_CURSES_DATA_COMPONENT_TYPE = REGISTRIES.register(
            Registries.DATA_COMPONENT_TYPE,
            "curses_revealed",
            () -> DataComponentType.<Unit>builder()
                    .persistent(Codec.unit(Unit.INSTANCE))
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
                    .build());

    static final TagFactory TAGS = TagFactory.make(SneakyCurses.MOD_ID);
    public static final TagKey<Item> REVEALS_CURSES_ITEM_TAG = TAGS.registerItemTag("reveals_curses");

    public static void bootstrap() {
        // NO-OP
    }
}
