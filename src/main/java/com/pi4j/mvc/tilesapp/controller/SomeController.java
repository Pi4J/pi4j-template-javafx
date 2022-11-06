package com.pi4j.mvc.tilesapp.controller;

import com.pi4j.mvc.tilesapp.model.SomeModel;
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
        updateModel(decrease(model.counter),
                    set(model.isActive, false));
    }

    public void setIsActive(boolean is){
        setValue(model.isActive, is);
    }

//    public void ledToggle(){
//        if (model.isLedActive.getValue()) {
//            setValue(model.isLedActive, false);
//        } else {
//            setValue(model.isLedActive, true);
//        }
//    }

    public void setButtonPressed(boolean is){
        setValue(model.isButtonPressed, is);
    }



//    public void ledOff(){
//        setValue(model.isActive, false);
//    }

    public void ledMessage(){
        System.out.println("Button pressed");
    }
}
