package com.pi4j.mvc.util.mvcbase;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A basic implementation of the Observable-Pattern.
 *
 * Be prepared to enhance this according to your requirements.
 */
public final class ObservableValue<V>  {
    // all these listeners will get notified whenever the value changes
    private final Set<ValueChangeListener<V>> listeners = new HashSet<>();

    private volatile V value;

    public ObservableValue(V initialValue) {
        value = initialValue;
    }

    /**
     * Registers a new observer (aka 'listener')
     *
     * @param listener specifies what needs to be done whenever the value is changed
     */
    public void onChange(ValueChangeListener<V> listener) {
        listeners.add(listener);
        listener.update(value, value);  // listener is notified immediately
    }

    /**
     * That's the core functionality of an 'ObservableValue'.
     *
     * Every time the value changes, all the listeners will be notified.
     *
     * This is method is 'package private', only 'ControllerBase' is allowed to set a new value.
     *
     * For the UIs setValue is not accessible
     *
     * @param newValue the new value
     */
    void setValue(V newValue) {
        if (Objects.equals(value, newValue)) {  // no notification if value hasn't changed
            return;
        }
        V oldValue = value;
        value = newValue;

        listeners.forEach(listener -> {
            if (value.equals(newValue)) { // pre-ordered listeners might have changed this and thus the callback no longer applies
                listener.update(oldValue, newValue);
            }
        });
    }

    /**
     * It's ok to make this public.
     *
     * @return the value managed by this ObservableValue
     */
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @FunctionalInterface
    public interface ValueChangeListener<V> {
        void update(V oldValue, V newValue);
    }
}
