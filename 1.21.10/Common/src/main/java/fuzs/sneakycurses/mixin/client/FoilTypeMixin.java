package fuzs.sneakycurses.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(ItemStackRenderState.FoilType.class)
abstract class FoilTypeMixin {
    @Nullable
    @Unique
    private static ItemStackRenderState.FoilType sneakycurses$standardCurse;
    @Nullable
    @Unique
    private static ItemStackRenderState.FoilType sneakycurses$specialCurse;

    @Invoker("<init>")
    private static ItemStackRenderState.FoilType sneakycurses$init(String name, int ordinal) {
        throw new RuntimeException();
    }

    @ModifyReturnValue(method = "$values()[Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;",
            at = @At("TAIL"))
    private static ItemStackRenderState.FoilType[] values(ItemStackRenderState.FoilType[] values) {
        // this can be chained for adding multiple values
        values = sneakycurses$appendValue(values, sneakycurses$standardCurse, "standard_curse");
        sneakycurses$standardCurse = values[values.length - 1];
        values = sneakycurses$appendValue(values, sneakycurses$specialCurse, "special_curse");
        sneakycurses$specialCurse = values[values.length - 1];
        return values;
    }

    @Unique
    private static ItemStackRenderState.FoilType[] sneakycurses$appendValue(ItemStackRenderState.FoilType[] values, @Nullable ItemStackRenderState.FoilType foilType, String name) {
        ItemStackRenderState.FoilType[] newValues = new ItemStackRenderState.FoilType[values.length + 1];
        System.arraycopy(values, 0, newValues, 0, values.length);
        newValues[values.length] = foilType != null ? foilType :
                sneakycurses$init("SNEAKYCURSES_" + name.toUpperCase(Locale.ROOT), values.length);
        return newValues;
    }
}
