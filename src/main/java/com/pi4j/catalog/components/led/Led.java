package com.pi4j.catalog.components.led;

public interface Led {
    void on();

    boolean isOn();

    void off();

    boolean toggle();

    void shutdown();
}
