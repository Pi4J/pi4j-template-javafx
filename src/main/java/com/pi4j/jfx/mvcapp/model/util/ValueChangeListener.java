package com.pi4j.jfx.mvcapp.model.util;

@FunctionalInterface
public interface ValueChangeListener<T> {
    void update(T oldValue, T newValue);
}
