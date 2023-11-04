package fuzs.sneakycurses.mixin;

import fuzs.sneakycurses.api.event.v1.FabricLivingEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void collectEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> callback, Map<EquipmentSlot, ItemStack> map, EquipmentSlot equipmentSlot, ItemStack oldItemStack, ItemStack newItemStack) {
        FabricLivingEvents.LIVING_EQUIPMENT_CHANGE.invoker().onLivingEquipmentChange(LivingEntity.class.cast(this), equipmentSlot, oldItemStack, newItemStack);
    }
}
