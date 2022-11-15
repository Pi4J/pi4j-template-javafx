package com.pi4j.mvc.tilesapp.controller;

import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;


public class SomeController extends ControllerBase<SomeModel> {

    public SomeController(SomeModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and from nowhere else)

    // Gives the model a boolean, if the button is pressed or not.
    public void setButtonPressed(boolean is) {
        setValue(model.isButtonPressed, is);
    }

    // Sends message as output
    public void whileMessage(String type){
        System.out.println(type+" button is still pressed");
    }

    public void pressedMessage(String type){
        System.out.println(type+" button is pressed");
    }
}
