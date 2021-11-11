package com.pi4j.jfx.util.mvc;

@FunctionalInterface
public interface ValueChangeListener<V> {
    void update(V oldValue, V newValue);
}
