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
    public final ObservableValue<Integer> counter    = new ObservableValue<>(73);
    public final ObservableValue<Boolean> isActive = new ObservableValue<>(false);
    public final ObservableValue<Boolean> isLedActive = new ObservableValue<>(false);
    public final ObservableValue<Boolean> isButtonPressed = new ObservableValue<>(false);

}
