package com.pi4j.jfx.util.mvc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A basic implementation of the Observable-Pattern
 *
 * Be prepared to enhance this according to your requirements.
 */
public final class ObservableValue<T>  {
    // all these listeners will get notified whenever the value changes
    private final Set<ValueChangeListener<T>> listeners = new HashSet<>();

    private volatile T value;

    public ObservableValue(T initialValue) {
        value = initialValue;
    }

    /**
     * Registers a new observer (aka 'listener')
     *
     * @param listener specifies what needs to be done whenever the value is changed
     */
    public void onChange(ValueChangeListener<T> listener) {
        listeners.add(listener);
        listener.update(null, value);  //  listener is notified immediately
    }

    /**
     * That's the core functionality of an 'ObservableValue'.
     *
     * Every times the value changes, all the listeners will be notified.
     *
     * This is package private, only 'ControllerBase' is allowed to set a new value.
     *
     * For the UIs setValue is not accessible
     *
     * @param newValue the new value
     */
    void setValue(T newValue) {
        if (Objects.equals(value, newValue)) {  // no notification if value hasn't changed
            return;
        }
        T oldValue = value;
        value      = newValue;

        listeners.forEach(listener -> listener.update(oldValue, newValue));
    }

    /**
     * It's ok to make this public.
     *
     * @return the value managed by this ObservableValue
     */
    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
