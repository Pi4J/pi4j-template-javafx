package com.pi4j.mvc.tilesapp.controller;

import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;


public class SomeController extends ControllerBase<SomeModel> {

    public SomeController(SomeModel model) {
        super(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and from nowhere else)

    // Übergibt dem Model ein Boolean, ob der Button gedrückt ist oder nicht.
    public void setButtonPressed(boolean is) {
        setValue(model.isButtonPressed, is);
    }

    // Sendet Nachricht auf die Konsole, falls der Button mit einer gegebenen Länge gedrückt bleibt.
    public void buttonMessage(){
        System.out.println("Button is still pressed");
    }
}
