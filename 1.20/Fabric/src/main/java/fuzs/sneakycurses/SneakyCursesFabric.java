package fuzs.sneakycurses;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.core.FabricEventInvokerRegistry;
import fuzs.sneakycurses.api.event.v1.FabricLivingEvents;
import fuzs.sneakycurses.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import net.fabricmc.api.ModInitializer;

public class SneakyCursesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        registerInvokers();
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
    }

    private static void registerInvokers() {
        FabricEventInvokerRegistry.INSTANCE.register(LivingEquipmentChangeCallback.class, FabricLivingEvents.LIVING_EQUIPMENT_CHANGE);
    }
}
