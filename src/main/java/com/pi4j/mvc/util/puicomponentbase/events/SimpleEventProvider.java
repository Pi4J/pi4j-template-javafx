package com.pi4j.mvc.util.puicomponentbase.events;

/**
 * Generic simple event provider with empty default implementation.
 * Implementing this interface in a class allows mapping raw event values to simple events.
 * Each implementation is responsible for actually triggering the simple events within {@link #dispatchSimpleEvents(Object)}.
 *
 * @param <E> Type of value which gets passed to event handlers.
 */
public interface SimpleEventProvider<E> {
    /**
     * Analyzes the given value passed by an event and triggers 0-n simple events based on it.
     * This method allows mapping various value/state changes to simple events.
     * Must be attached using {@link DigitalEventProvider#addListener(EventHandler)}.
     *
     * @param value    Event value
     */
    default void dispatchSimpleEvents(E value) {
    }
}
