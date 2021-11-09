package com.pi4j.jfx.mvcapp.model;

import com.pi4j.jfx.mvcapp.model.util.ControllerBase;

public class ExampleController extends ControllerBase<ExampleModel> {

    public ExampleController(ExampleModel model) {
        super(model);
    }

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
