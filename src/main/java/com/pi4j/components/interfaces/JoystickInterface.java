package com.pi4j.components.interfaces;

public interface JoystickInterface {
    void onNorth(Runnable handler);

    void whileNorth(long millis, Runnable method);

    void onWest(Runnable handler);

    void whileWest(long millis, Runnable method);

    void onSouth(Runnable handler);

    void whileSouth(long millis, Runnable method);

    void onEast(Runnable handler);

    void whileEast(long millis, Runnable method);

    void onPushDown(Runnable handler);

    void onPushUp(Runnable method);

    void pushWhilePushed(long millis, Runnable method);

    void deRegisterAll();
}
