package com.pi4j.jfx.mvcapp.model.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Eine einfache Implementierung des Observable-Patterns
 *
 * BITTE BEACHTEN SIE: diese Implementierung ist fuer den Unterricht gedacht und zeigt den Kern einer moeglichen Implementierung.
 * Fuer den Einsatz in einem produktiven System reicht sie in der Regel nicht aus.
 *
 * @author Dieter Holz
 */
public class ObservableValue<T>  {
    private final Set<ValueChangeListener<T>> listeners = new HashSet<>();

    private T value;

    public ObservableValue(T initialValue) {
        value = initialValue;
    }

    public void onChange(ValueChangeListener<T> listener) {
        listeners.add(listener);
        listener.update(null, value);  // der listener erhaelt sofort den aktuellen Wert
    }

    public void removeOnChange(ValueChangeListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * That's the core functionality of n  'ObservableValue'.
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
        if (Objects.equals(value, newValue)) {  // keine Benachrichtigung falls sich der Wert nicht geaendert hat
            return;
        }
        T oldValue = value;
        value      = newValue;

        listeners.forEach(listener -> listener.update(oldValue, newValue));
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
