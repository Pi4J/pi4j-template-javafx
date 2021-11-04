package com.pi4j.fxgl.util;

import java.time.Duration;

/**
 * Gives you a more convenient API to access a real phyical button attached to GPIO.
 *
 * Typically, the methods end up in Pi4J calls.
 *
 * @author Dieter Holz
 */
public interface HardwareButton {
    /**
     * Default time used in whileDown events to trigger the next event
     */
    Duration DEFAULT_REFRESH_RATE = Duration.ofMillis(80);

    HardwareButton getButtonDelegate();

    /**
     * Do all the necessary initialization
     * mainly: create the Pi4J-DigitalInput to get access to the physical button
     */
    default void initialize() {
        getButtonDelegate().initialize();
    }

    default void shutdown(){
        getButtonDelegate().shutdown();
    }


    /**
     * Adds the action to a list of things that are done whenever the button is pressed
     *
     * The action gets triggered whenever the button is pressed.
     *
     * @param action whatever needs to be done
     */
    default void addOnPressed(Runnable action) {
        getButtonDelegate().addOnPressed(action);
    }

    /**
     * Adds the action to a list of things that are done whenever the button is depressed.
     *
     * The action gets triggered whenever the button is depressed.
     *
     * @param action whatever needs to be done
     */
    default void addOnReleased(Runnable action) {
        getButtonDelegate().addOnReleased(action);
    }


    default void addWhileDown(Runnable action, HardwareButton... alsoPressedButton) {
        addWhileDown(action, DEFAULT_REFRESH_RATE, alsoPressedButton);
    }

    /**
     * Adds the action to a list of things that are done while the button and all of the 'alsoPressedButtons' are pressed .
     *
     * The action gets triggered periodically with the given 'pulse'.
     *
     * @param action whatever needs to be done
     */
    default void addWhileDown(Runnable action, Duration pulse, HardwareButton... alsoPressedButton) {
        getButtonDelegate().addWhileDown(action, pulse, alsoPressedButton);
    }

    /**
     * Checks if button is currently pressed
     *
     * @return True if button is pressed
     */
    default boolean isPressed() {
        return getButtonDelegate().isPressed();
    }

    /**
     * Checks if button is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    default boolean isDepressed() {
        return getButtonDelegate().isDepressed();
    }

    /**
     *
     * @return the button's name
     */
    default String getLabel() {
        return getButtonDelegate().getLabel();
    }

}
