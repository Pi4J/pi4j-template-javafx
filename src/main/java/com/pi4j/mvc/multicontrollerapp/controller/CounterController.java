package com.pi4j.mvc.multicontrollerapp.controller;

import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;

/**
 * Handles all the functionality needed to manage the 'counter'.
 *
 * All methods are intentionally 'package private'. Only 'ApplicationController' can access them.
 */
class CounterController extends ControllerBase<ExampleModel> {

    CounterController(ExampleModel model) {
        super(model);
    }

    // the logic we need for managing the counter

    void increaseCounter() {
        increase(model.counter);
    }

    void decreaseCounter() {
        decrease(model.counter);
    }
}
