package com.pi4j.mvc.util.puicomponentbase.events;

import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;

/**
 * Generic digital event provider with default implementation for digital components.
 * Implementing this interface in a component class provides the user with the ability to add and remove event listeners.
 * <p>
 * Transformation of raw {@link DigitalState} into target type {@link V} is made by {@link #mapDigitalState(DigitalState)}.
 * The method {@link #getDigitalInput()} must return the {@link DigitalInput} instance where the listener should be added.
 *
 * @param <V> Type of value which gets passed to event handlers.
 */
public interface DigitalEventProvider<V> extends EventProvider<DigitalEventListener<V>, V>, SimpleEventProvider<V> {
    /**
     * Returns the Pi4J {@link DigitalInput} associated with this component.
     *
     * @return Pi4J {@link DigitalInput} instance
     */
    DigitalInput getDigitalInput();

    /**
     * Maps the Pi4J {@link DigitalState} to the target type {@link V}.
     *
     * @param digitalState Pi4J digital state to map
     * @return Mapped component state
     */
    V mapDigitalState(DigitalState digitalState);

    /**
     * Adds a new event listener to this component.
     *
     * @param handler Event handler to call
     * @return Event listener instance
     */
    @Override
    default DigitalEventListener<V> addListener(EventHandler<V> handler) {
        return new DigitalEventListener<>(getDigitalInput(), this::mapDigitalState, handler);
    }

    /**
     * Removes an existing event listener from this component.
     *
     * @param listener Event listener to remove
     */
    @Override
    default void removeListener(DigitalEventListener<V> listener) {
        getDigitalInput().removeListener(listener);
    }
}
