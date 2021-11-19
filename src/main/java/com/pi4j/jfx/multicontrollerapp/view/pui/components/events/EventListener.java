package com.pi4j.jfx.multicontrollerapp.view.pui.components.events;

/**
 * Generic event listener interface for easy removal of existing listeners.
 */
public interface EventListener {
    /**
     * Removes the listener and therefore prevents any execution in the future.
     */
    void remove();
}
