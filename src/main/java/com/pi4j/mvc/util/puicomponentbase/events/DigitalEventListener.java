package com.pi4j.mvc.util.puicomponentbase.events;

import java.util.function.Function;

import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;

/**
 * Generic implementation of {@link EventListener} for Pi4J {@link DigitalInput} instances.
 * Can be registered as a {@link DigitalStateChangeListener} within Pi4J.
 * <p>
 * Whenever a Pi4J {@link DigitalStateChangeEvent} occurs, the provided {@link DigitalState} gets converted into the target type {@link V}
 * by calling the provided {@link #mapper} function. This mapped value then gets passed to the provided {@link EventHandler} to actually
 * handle the event with the function provided by the user.
 *
 * @param <V> Target type of event value which gets passed to event handler.
 */
public class DigitalEventListener<V> implements EventListener, DigitalStateChangeListener {
    /**
     * Pi4J digital input to which this digital event listener has been attached
     */
    protected final DigitalInput digitalInput;

    /**
     * Mapper function which transforms {@link DigitalState} into {@link V}
     */
    protected final Function<DigitalState, V> mapper;

    /**
     * Handler function which gets called
     */
    protected final EventHandler<V> handler;

    /**
     * Creates a new digital event listener for the given digital input which uses the provided mapper and event handler.
     * This class instance gets automatically registered as a {@link DigitalStateChangeListener} for the passed {@link DigitalInput}.
     *
     * @param digitalInput Digital input where event listener gets attached
     * @param mapper       Mapping function from {@link DigitalState} to target type {@link V}
     * @param handler      Handler function which gets called
     */
    public DigitalEventListener(DigitalInput digitalInput, Function<DigitalState, V> mapper, EventHandler<V> handler) {
        this.digitalInput = digitalInput.addListener(this);
        this.mapper = mapper;
        this.handler = handler;
    }

    /**
     * Handles incoming Pi4J events by calling our own {@link #handler} with the mapped value from {@link #mapper}.
     *
     * @param event Pi4J event
     */
    @Override
    public void onDigitalStateChange(DigitalStateChangeEvent event) {
        handler.handle(mapper.apply(event.state()));
    }

    /**
     * Removes this digital event listener, therefore preventing any future execution.
     */
    @Override
    public void remove() {
        digitalInput.removeListener(this);
    }
}
