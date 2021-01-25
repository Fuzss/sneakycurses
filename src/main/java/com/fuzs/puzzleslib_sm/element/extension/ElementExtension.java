package com.fuzs.puzzleslib_sm.element.extension;

import com.fuzs.puzzleslib_sm.element.AbstractElement;
import com.fuzs.puzzleslib_sm.element.IEventListener;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * abstract template for sided elements complementing a common element
 */
public abstract class ElementExtension<T extends ExtensibleElement<?>> implements IEventListener {

    /**
     * common element this belongs to
     */
    private final T parent;

    /**
     * create new with parent
     * @param parent parent
     */
    public ElementExtension(T parent) {

        this.parent = parent;
    }

    /**
     * @return common parent for this
     */
    public T getParent() {

        return this.parent;
    }

    @Override
    public List<AbstractElement.EventStorage<? extends Event>> getEvents() {

        return this.getParent().getEvents();
    }

}
