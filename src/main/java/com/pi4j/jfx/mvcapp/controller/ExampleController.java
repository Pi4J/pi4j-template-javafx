package com.pi4j.jfx.mvcapp.controller;

import com.pi4j.jfx.mvcapp.model.ExampleModel;
import com.pi4j.jfx.util.mvc.ControllerBase;

public class ExampleController extends ControllerBase<ExampleModel> {

    /**
     * ExampleController works on a ExampleModel
     *
     * @param model the model that is managed by this controller
     */
    public ExampleController(ExampleModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and nothing else)

    public void increaseCounter() {
        setValue(model.counter, model.counter.getValue() + 1);
    }

    public void decreaseCounter() {
        setValue(model.counter, model.counter.getValue() - 1);
    }

    public void setLedGlows(boolean glows) {
        setValue(model.ledGlows, glows);
    }

    public void blink() {
        toggle(model.blinkingTrigger);
    }
}
