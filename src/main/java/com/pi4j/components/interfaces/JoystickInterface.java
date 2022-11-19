package com.pi4j.components.interfaces;

public interface JoystickInterface {
    void onNorthDown(Runnable handler);

    void onNorthUp(Runnable handler);

    void whileNorth(long millis, Runnable method);

    void onWestDown(Runnable handler);

    void onWestUp(Runnable handler);

    void whileWest(long millis, Runnable method);

    void onSouthDown(Runnable handler);

    void onSouthUp(Runnable handler);

    void whileSouth(long millis, Runnable method);

    void onEastDown(Runnable handler);

    void onEastUp(Runnable handler);

    void whileEast(long millis, Runnable method);

    void onPushDown(Runnable handler);

    void onPushUp(Runnable method);

    void pushWhilePushed(long millis, Runnable method);

    void deRegisterAll();
}
