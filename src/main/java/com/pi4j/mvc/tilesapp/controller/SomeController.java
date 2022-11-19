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

    //turn off ledbutton led
    public void setLedButtonReleased() {
        setValue(model.isLedButtonActive, false);
    }

    //switch led of ledbutton on and off depending on delay
    public void whilePressedLedButton() {
        setValue(model.isLedButtonActive, !model.isLedButtonActive.getValue());
    }

    // Sends message as output
    public void whileMessage(String type){
        System.out.println(type+" button is still pressed");
    }

    public void buttonMessage(String type, boolean isPressed){
        String state = "released";
        if(isPressed) {
             state = "pressed";
        }

        System.out.println(type +" button is "+ state);
    }
}
