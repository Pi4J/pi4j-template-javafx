package com.pi4j.components.interfaces;

import java.util.function.Consumer;

public interface JoystickAnalogInterface {
    void xOnMove(Consumer<Double> task);

    void yOnMove(Consumer<Double> task);

    void pushOnDown(Runnable task);

    void pushOnUp(Runnable task);

    void pushWhilePressed(Runnable task, long whilePressedDelay);

    void deregisterAll();

    void calibrateJoystick();

    void start(double threshold, int readFrequency);

    void stop();
}
