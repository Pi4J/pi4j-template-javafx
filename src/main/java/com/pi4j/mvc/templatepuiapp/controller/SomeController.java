package com.pi4j.mvc.templatepuiapp.controller;

import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;

public class SomeController extends ControllerBase<SomeModel> {

    protected final int terminationCount = 10;

    public SomeController(SomeModel model) {
        super(model);
    }

    public void activate(){
        setValue(model.busy, true);
    }

    public void deactivate(){
        // use 'updateModel' if you need to set multiple values
        updateModel(set(model.busy, false),
                    increase(model.counter));

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
