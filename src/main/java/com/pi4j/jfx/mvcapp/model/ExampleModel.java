package com.pi4j.jfx.mvcapp.model;

import com.pi4j.jfx.mvcapp.model.util.ObservableValue;

/**
 * In MVC the 'Model' mainly consists of 'ObservableValues', there should be no need for additional methods
 * because all the application logic is handled by the 'Controller'
 */
public class ExampleModel  {
    public final ObservableValue<String>  systemInfo      = new ObservableValue<>("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
    public final ObservableValue<Integer> counter         = new ObservableValue<>(73);
    public final ObservableValue<Boolean> ledGlows        = new ObservableValue<>(false);
    public final ObservableValue<Boolean> blinkingTrigger = new ObservableValue<>(false);
}
