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

    //Observes if the Button is pressed or not
    public final ObservableValue<Boolean> isButtonPressed = new ObservableValue<>(false);

    // Observers if the LED-Button is active
    public final ObservableValue<Boolean> isLedButtonActive = new ObservableValue<>(false);

    public final ObservableValue<Double> currentXPosition = new ObservableValue<>(0.0);

    public final ObservableValue<Double> currentYPosition = new ObservableValue<>(0.0);

    public final ObservableValue<Double> currentPotiPosition = new ObservableValue<>(0.0);
}
