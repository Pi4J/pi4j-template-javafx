package com.pi4j.mvc.util.puicomponentbase.events;

/**
 * Generic functional interface for simple event handlers, which are event handlers without a parameter.
 * Usually supposed to be called / triggered within {@link SimpleEventProvider#dispatchSimpleEvents(Object)}
 */
@FunctionalInterface
public interface SimpleEventHandler {
    /**
     * Handles a specific simple event based on implementation needs.
     * This method does not take any parameters and returns no value either.
     * For more advanced event handling, use {@link EventHandler}.
     */
    void handle();
}
