package com.fuzs.puzzleslib_sm.element.extension;

import com.fuzs.puzzleslib_sm.element.side.IClientElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

/**
 * an element that can be extended to the client
 * @param <T> extension class
 */
public abstract class ClientExtensibleElement<T extends ElementExtension<?> & IClientElement> extends ExtensibleElement<T> implements IClientElement {

    /**
     * @param extension provider for extension
     */
    public ClientExtensibleElement(Function<ExtensibleElement<?>, T> extension) {

        super(extension, Dist.CLIENT);
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
