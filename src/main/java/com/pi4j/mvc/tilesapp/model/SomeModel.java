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
    //TODO: MODEL-GUI
    //Observes if the LED is Active or not
    public final ObservableValue<Boolean> isLedActive = new ObservableValue<>(false);

    //Observes if the Button is pressed or not
    public final ObservableValue<Boolean> isButtonPressed = new ObservableValue<>(false);

    public final ObservableValue<Boolean> isLedButtonActive = new ObservableValue<>(false);

}
