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
        increaseValue(model.counter);
    }

    public void decreaseCounter() {
        //use updateModel if several values need an update in this action
        updateModel(decrease(model.counter),
                    set(model.isActive, false));
    }

    public void setIsActive(boolean is){
        //use setValue if a single value needs to be updated in this action
        setValue(model.isActive, is);
    }
}
