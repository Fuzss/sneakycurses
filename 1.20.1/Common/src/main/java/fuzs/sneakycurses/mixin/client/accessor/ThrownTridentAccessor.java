package fuzs.sneakycurses.mixin.client.accessor;

import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThrownTrident.class)
public interface ThrownTridentAccessor {

    @Accessor("tridentItem")
    ItemStack sneakycurses$getTridentItem();

    @Accessor("tridentItem")
    void sneakycurses$setTridentItem(ItemStack tridentItem);
}
