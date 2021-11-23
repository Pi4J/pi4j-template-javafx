package com.pi4j.mvc.templateapp.controller;

import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;


public class SomeController extends ControllerBase<SomeModel> {

    public SomeController(SomeModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and from nowhere else)

    public void increaseCounter() {
        setValue(model.counter, model.counter.getValue() + 1);
    }

    public void decreaseCounter() {
        setValue(model.counter, model.counter.getValue() - 1);
    }

    public void setLedGlows(boolean glows){
        setValue(model.ledGlows, glows);
    }
}
