package fuzs.sneakycurses.mixin.client;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    public void isFoil$head(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!CurseMatcher.isAffected(stack)) return;
        if (!SneakyCurses.CONFIG.client().disguiseItem) return;
        if (CurseMatcher.allEnchantsAreCursed(stack)) {
            callbackInfo.setReturnValue(false);
        }
    }
}
