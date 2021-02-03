package com.fuzs.puzzleslib_sm.capability;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

/**
 * helper object for registering and attaching mod capabilities, needs to be extended by every mod individually
 */
public abstract class CapabilityController {

    private final ArrayListMultimap<Class<?>, ICapabilityProvider> defers = ArrayListMultimap.create();

    /**
     * create new object just for registering and adding listeners
     */
    public CapabilityController() {

        this.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * register capabilties by calling {@link #register(Class, Callable)}
     */
    protected abstract void register();

    /**
     * attack capabilities to an object
     * @param evt capabilities event
     */
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onAttachCapabilities(final AttachCapabilitiesEvent<?> evt) {

        evt.getObject()
    }

    /**
     * attack capabilities to entity
     * @param evt event for entity
     */
    protected void onAttachEntityCapabilities(final AttachCapabilitiesEvent<Entity> evt) {

        evt.addCapability();
    }

    /**
     * attack capabilities to tile entity
     * @param evt event for tile entity
     */
    protected void onAttachTileEntityCapabilities(final AttachCapabilitiesEvent<TileEntity> evt) {

    }

    /**
     * attack capabilities to world
     * @param evt event for world
     */
    protected void onAttachWorldCapabilities(final AttachCapabilitiesEvent<World> evt) {

    }

    /**
     * attack capabilities to chunk
     * @param evt event for chunk
     */
    protected void onAttachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> evt) {

    }

    /**
     * register new capability to {@link CapabilityManager} instance, to be called from {@link #register()}
     * @param type class of generic type
     * @param factory method reference
     * @param <T> generic type
     */
    protected static <T> void register(Class<T> type, Callable<? extends T> factory) {

        CapabilityManager.INSTANCE.register(type, new CapabilityStorage<>(), factory);
    }

    /**
     * key for {@link #register()}
     * @param modId owner mod id
     * @param name name in upper camel case format
     * @return resource location from mod id and name as lower underscore format
     */
    protected static ResourceLocation getRegistryKey(String modId, String name) {

        String formattedName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        return new ResourceLocation(modId, formattedName);
    }

    /**
     * overload to avoid always null problems
     * @param provider provider object
     * @param cap capability to get
     * @param <T> capability type
     * @return capability object
     */
    @Nonnull
    public static <T> LazyOptional<T> getCapability(ICapabilityProvider provider, Capability<T> cap) {

        return provider.getCapability(cap);
    }

}
