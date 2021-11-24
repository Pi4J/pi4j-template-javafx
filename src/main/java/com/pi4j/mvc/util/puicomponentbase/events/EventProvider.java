package com.pi4j.mvc.util.puicomponentbase.events;

/**
 * Generic event provider interface for components which expose the ability to add or remove event listeners.
 *
 * @param <L> Type of event listener used for implementation.
 * @param <V> Type of value which gets passed to event handlers.
 */
public interface EventProvider<L extends EventListener, V> {
    /**
     * Adds a new event listener which uses the given handler.
     *
     * @param handler Event handler to call
     * @return Event listener instance
     */
    L addListener(EventHandler<V> handler);

    /**
     * Removes the given event listener.
     *
     * @param listener Event listener to remove
     */
    void removeListener(L listener);
}
