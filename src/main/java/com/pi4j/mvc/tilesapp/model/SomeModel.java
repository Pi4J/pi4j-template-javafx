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
    public final ObservableValue<String>  systemInfo = new ObservableValue<>("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
    public final ObservableValue<Integer> counter    = new ObservableValue<>(73);
    public final ObservableValue<Boolean> isActive   = new ObservableValue<>(false);
}
