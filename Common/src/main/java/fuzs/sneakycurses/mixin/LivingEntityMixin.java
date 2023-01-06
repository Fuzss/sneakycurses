package fuzs.sneakycurses.mixin;

import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onEquipItem", at = @At("HEAD"))
    public void onEquipItem(EquipmentSlot equipmentSlot, ItemStack oldStack, ItemStack newStack, CallbackInfo callback) {
        CurseRevealHandler.tryRevealCurses(newStack, this.random);
        if (((Object) this) instanceof ServerPlayer player) {
            player.displayClientMessage(Component.translatable("").withStyle(ChatFormatting.RED), false);
        }
    }
}
