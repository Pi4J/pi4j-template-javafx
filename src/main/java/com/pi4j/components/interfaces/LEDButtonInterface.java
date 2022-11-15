package com.pi4j.components.interfaces;

public interface LEDButtonInterface {
    void LEDsetStateOn();

    void LEDsetStateOff();

    void onDown(Runnable method);

    void onUp(Runnable method);

    void btnwhilePressed(Runnable method, long millis);
}
