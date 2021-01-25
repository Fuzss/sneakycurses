package com.fuzs.puzzleslib_sm.element.extension;

import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.element.side.IClientElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.Function;

public abstract class ClientExtensibleElement<T extends ElementExtension<?> & IClientElement> extends ExtensibleElement<T> implements IClientElement {

    public ClientExtensibleElement(Function<ExtensibleElement<?>, T> extension) {

        super(extension, Dist.CLIENT);
    }

    @Override
    protected final void setupExtensionConfig(String elementId) {

        ConfigManager.builder().create(elementId, this::setupClientConfig, ModConfig.Type.CLIENT, this.getClientDescription());
    }

    @Override
    protected final void setupExtension() {

        this.setupClient();
    }

    @Override
    public final void setupClient() {

        this.extension.setupClient();
    }

    @Override
    public final void loadClient() {

        this.extension.loadClient();
    }

    @Override
    public final void setupClientConfig(ForgeConfigSpec.Builder builder) {

        this.extension.setupClientConfig(builder);
    }

    @Override
    public final String[] getClientDescription() {

        return this.extension.getClientDescription();
    }

}
