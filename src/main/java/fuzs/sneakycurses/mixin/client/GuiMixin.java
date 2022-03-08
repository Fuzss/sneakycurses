package fuzs.sneakycurses.mixin.client;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    protected ItemStack lastToolHighlight;

    @ModifyVariable(method = "renderSelectedItemName", at = @At("STORE"), ordinal = 0)
    public MutableComponent renderSelectedItemName$storeHoverNameComponent(MutableComponent mutablecomponent) {
        if (!CurseMatcher.isAffected(this.lastToolHighlight)) return mutablecomponent;
        if (!SneakyCurses.CONFIG.client().colorName) return mutablecomponent;
        return mutablecomponent.withStyle(ChatFormatting.RED);
    }
}
