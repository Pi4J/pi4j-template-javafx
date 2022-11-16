package com.pi4j.components.interfaces;

public interface SimpleButtonInterface {
    void onDown(Runnable task);

    void onUp(Runnable task);

    void whilePressed(Runnable task, long whilePressedDelay);

    void deRegisterAll();
}
