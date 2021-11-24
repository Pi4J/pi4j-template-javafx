package com.pi4j.mvc.util.puicomponentbase.events;

/**
 * Generic functional interface used for for event handlers.
 *
 * @param <V> Type of event value
 */
@FunctionalInterface
public interface EventHandler<V> {
    /**
     * Handles an event based on implementation needs.
     *
     * @param value Event value
     */
    void handle(V value);
}
