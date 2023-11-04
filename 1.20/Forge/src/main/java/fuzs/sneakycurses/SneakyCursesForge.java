package fuzs.sneakycurses;

import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.api.event.v1.core.ForgeEventInvokerRegistry;
import fuzs.sneakycurses.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import fuzs.sneakycurses.data.ModItemTagsProvider;
import fuzs.sneakycurses.init.ModRegistry;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SneakyCurses.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SneakyCursesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerInvokers();
        ModConstructor.construct(SneakyCurses.MOD_ID, SneakyCurses::new);
        registerCapabilities();
        DataProviderHelper.registerDataProviders(SneakyCurses.MOD_ID, ModItemTagsProvider::new);
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.CURSE_REVEAL_CAPABILITY, new CapabilityToken<>() {
            // NO-OP
        });
    }

    private static void registerInvokers() {
        ForgeEventInvokerRegistry.INSTANCE.register(LivingEquipmentChangeCallback.class, LivingEquipmentChangeEvent.class, (LivingEquipmentChangeCallback callback, LivingEquipmentChangeEvent evt) -> {
            callback.onLivingEquipmentChange(evt.getEntity(), evt.getSlot(), evt.getFrom(), evt.getTo());
        });
    }
}
