package com.pi4j.jfx.util.mvc;

@FunctionalInterface
public interface ValueChangeListener<T> {
    void update(T oldValue, T newValue);
}
