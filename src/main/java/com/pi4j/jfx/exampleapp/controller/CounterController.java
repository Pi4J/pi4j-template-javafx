package com.pi4j.jfx.exampleapp.controller;

import com.pi4j.jfx.exampleapp.model.ExampleModel;
import com.pi4j.jfx.util.mvc.ControllerBase;

class CounterController extends ControllerBase<ExampleModel> {

    /**
     * Todo
     *
     * @param model the model that is managed by this controller
     */
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
