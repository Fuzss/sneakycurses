package com.fuzs.sneakymagic.common.element;

import com.fuzs.puzzleslib_sm.element.extension.ClientExtensibleElement;
import com.fuzs.sneakymagic.client.element.CursesClientElement;

public class CursesElement extends ClientExtensibleElement<CursesClientElement> {

    public CursesElement() {

        super(element -> new CursesClientElement((CursesElement) element));
    }

    @Override
    public String getDescription() {

        return "Apply handy buffs and improvements to a few enchantments.";
    }

}
