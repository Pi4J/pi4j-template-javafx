package com.pi4j.jfx.templatepuiapp.controller;

import com.pi4j.jfx.templatepuiapp.model.SomeModel;
import com.pi4j.jfx.util.mvc.ControllerBase;

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
        if(model.counter.getValue() >= terminationCount){
            terminate();
        }
    }

    protected void terminate() {
        System.exit(0);
    }
}
