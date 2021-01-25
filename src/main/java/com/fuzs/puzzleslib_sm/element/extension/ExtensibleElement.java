package com.fuzs.puzzleslib_sm.element.extension;

import com.fuzs.puzzleslib_sm.config.ConfigManager;
import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.puzzleslib_sm.element.side.ISidedElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Function;

public abstract class ExtensibleElement<T extends ElementExtension<?> & ISidedElement> extends AbstractElement implements ICommonElement {

    protected final T extension;

    public ExtensibleElement(Function<ExtensibleElement<?>, T> extension, Dist dist) {

        this.extension = dist == FMLEnvironment.dist ? extension.apply(this) : null;
    }

    @Override
    protected final void setupConfig(String elementId) {

        ConfigManager.builder().create(elementId, this::setupCommonConfig, ModConfig.Type.COMMON, this.getCommonDescription());
        if (this.isActive()) {

            this.setupExtensionConfig(elementId);
        }
    }

    @Override
    protected final void setupEvents() {

        this.setupCommon();
        if (this.isActive()) {

            this.setupExtension();
        }
    }

    protected abstract void setupExtensionConfig(String elementId);

    protected abstract void setupExtension();

    private boolean isActive() {

        return this.extension != null;
    }

}
