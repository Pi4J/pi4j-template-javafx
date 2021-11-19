package com.pi4j.jfx.multicontrollerapp.controller;

import com.pi4j.jfx.multicontrollerapp.model.ExampleModel;
import com.pi4j.jfx.util.mvc.ControllerBase;

/**
 * Handles all the functionality needed to manage the 'counter'
 *
 * All methods are intentionally 'package private'. Only 'ApplicationController' can access them
 */
class CounterController extends ControllerBase<ExampleModel> {

    CounterController(ExampleModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and nothing else)

    void increaseCounter() {
        setValue(model.counter, model.counter.getValue() + 1);
    }

    void decreaseCounter() {
        setValue(model.counter, model.counter.getValue() - 1);
    }
}
