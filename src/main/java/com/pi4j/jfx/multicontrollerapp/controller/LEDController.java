package com.pi4j.jfx.multicontrollerapp.controller;

import java.time.Duration;

import com.pi4j.jfx.multicontrollerapp.model.ExampleModel;
import com.pi4j.jfx.util.mvc.ControllerBase;

/**
 * Handles all the functionality needed to manage the 'led'
 *
 * All methods are intentionally 'package private'. Only 'ApplicationController' can access them
 */
class LEDController extends ControllerBase<ExampleModel> {

    LEDController(ExampleModel model) {
        super(model);
    }

    void setLedGlows(boolean glows) {
        setValue(model.ledGlows, glows);
    }

    /**
     * Controller controls all the state changes.
     */
    void blink() {
        final Duration duration = Duration.ofMillis(500);
        setLedGlows(false);
        for (int i = 0; i < 4; i++) {
            setLedGlows(true);
            pauseExecution(duration);
            setLedGlows(false);
            pauseExecution(duration);
        }
    }

    /**
     * Example for triggering some built-in action in PUI.
     *
     * Controller can't call PUI-component methods directly.
     *
     * Use an ObservableValue<Boolean> to trigger the action.
     */
//    public void blinkViaBuiltInAction() {
//        toggle(model.blinkingTrigger);
//    }
}
