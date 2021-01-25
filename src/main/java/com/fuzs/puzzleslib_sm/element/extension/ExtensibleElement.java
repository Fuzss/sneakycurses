package com.fuzs.puzzleslib_sm.element.extension;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.side.ICommonElement;
import com.fuzs.puzzleslib_sm.element.side.ISidedElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Function;

public abstract class ExtensibleElement<T extends ElementExtension<?> & ISidedElement> extends AbstractElement implements ICommonElement {

    protected final T extension;

    public ExtensibleElement(Function<ExtensibleElement<?>, T> extension, Dist dist) {

        this.extension = dist == FMLEnvironment.dist ? extension.apply(this) : null;
    }

}
