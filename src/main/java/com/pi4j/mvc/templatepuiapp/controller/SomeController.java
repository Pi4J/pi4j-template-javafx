package com.pi4j.mvc.templatepuiapp.controller;

import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;

public class SomeController extends ControllerBase<SomeModel> {

    protected final int terminationCount = 10;

    public SomeController(SomeModel model) {
        super(model);
    }

    public void ledOn(){
        setValue(model.ledGlows, true);
    }

    public void ledOff(){
        setValue(model.ledGlows, false);
        increase(model.counter);

        //using 'runLater' assures that new value is set on model
        runLater(m -> {
                     if (m.counter.getValue() > terminationCount) {
                         terminate();
                     }
                 });
    }

    protected void terminate() {
        System.exit(0);
    }
}
