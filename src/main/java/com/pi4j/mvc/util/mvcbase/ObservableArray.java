package com.pi4j.mvc.util.mvcbase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ObservableArray<V> {
    /** all these listeners will get notified whenever the value changes */
    private final Set<ValueChangeListener<V>> listeners = new HashSet<>();

    /**
     * The actual values of the array
     */
    private volatile V[] values;

    /**
     * Default Constructor
     * @param initialValues the initial array
     */
    public ObservableArray(V[] initialValues) {
        values = initialValues;
    }

    /**
     * Registers a new observer (aka 'listener')
     *
     * @param listener specifies what needs to be done whenever the value is changed
     */
    public void onChange(ValueChangeListener<V> listener) {
        listeners.add(listener);
        listener.update(values, values);  // listener is notified immediately
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
     * @param newValues the new value
     */
    void setValues(V[] newValues) {
        if (Arrays.equals(values, newValues)) {  // no notification if value hasn't changed
            return;
        }
        V[] oldValue = values;
        values = newValues;

        listeners.forEach(listener -> {
            if (Arrays.equals(values, newValues)) { // pre-ordered listeners might have changed this and thus the callback no longer applies
                listener.update(oldValue, newValues);
            }
        });
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
    void setValue(int position, V newValue) {
        if (Objects.equals(values[position], newValue)) {  // no notification if value hasn't changed
            return;
        }
        V[] oldValues = values;
        values[position] = newValue;

        listeners.forEach(listener -> listener.update(oldValues, values));
    }

    /**
     * It's ok to make this public.
     *
     * @return the value managed by this ObservableValues
     */
    public V[] getValues() {
        return values;
    }

    /**
     * It's ok to make this public.
     *
     * @return the value managed by this ObservableValue
     */
    public V getValue(int position) {
        return values[position];
    }

    /**
     * Giving the array out as a String
     * @return String with the values of the array
     */
    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @FunctionalInterface
    public interface ValueChangeListener<V> {
        void update(V[] oldValue, V[] newValue);
    }
}
