package com.pi4j.mvc.multicontrollerapp.model;

import com.pi4j.mvc.util.mvcbase.ObservableValue;

/**
 * In MVC the 'Model' mainly consists of 'ObservableValues'.
 *
 * There should be no need for additional methods.
 *
 * All the application logic is handled by the 'Controller'
 */
public class ExampleModel  {
    public final ObservableValue<String>  systemInfo      = new ObservableValue<>("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
    public final ObservableValue<Integer> counter         = new ObservableValue<>(73);
    public final ObservableValue<Boolean> ledGlows        = new ObservableValue<>(false);

    // if you want to use the LED's built-in blinking feature (instead of implementing blinking in Controller), you need an additional state
   // public final ObservableValue<Boolean> blinkingTrigger = new ObservableValue<>(false);
}
