package fuzs.sneakycurses.api.event.v1;

import fuzs.puzzleslib.api.event.v1.core.FabricEventFactory;
import fuzs.sneakycurses.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.world.entity.LivingEntity;

public final class FabricLivingEvents {
    /**
     * Fires whenever equipment changes are detected on an entity in {@link LivingEntity#collectEquipmentChanges()} from {@link LivingEntity#tick()}.
     */
    public static final Event<LivingEquipmentChangeCallback> LIVING_EQUIPMENT_CHANGE = FabricEventFactory.create(LivingEquipmentChangeCallback.class);

    private FabricLivingEvents() {

    }
}
