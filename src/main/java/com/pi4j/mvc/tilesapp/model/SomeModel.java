package com.pi4j.mvc.tilesapp.model;

import com.pi4j.mvc.util.mvcbase.ObservableValue;

/**
 * In MVC the 'Model' mainly consists of 'ObservableValues'.
 * <p>
 * There should be no need for additional methods.
 * <p>
 * All the application logic is handled by the 'Controller'
 */
public class SomeModel {
    // Observes if the LED is active
    public final ObservableValue<Boolean> isLedActive = new ObservableValue<>(false);

    // Observes if the button is pressed
    public final ObservableValue<Boolean> isButtonPressed = new ObservableValue<>(false);

    // Observers if the LED-Button is active
    public final ObservableValue<Boolean> isLedButtonActive = new ObservableValue<>(false);

    // Observes the normalized X-position of the Joystick analog
    public final ObservableValue<Double> currentXPosition = new ObservableValue<>(0.0);

    // Observes the normalized Y-position of the Joystick analog
    public final ObservableValue<Double> currentYPosition = new ObservableValue<>(0.0);

    // Observes the position of the potentiometer
    public final ObservableValue<Double> currentPotiPosition = new ObservableValue<>(0.0);
}
