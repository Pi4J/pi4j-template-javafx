package com.pi4j.jfx.exampleapp.controller;

import com.pi4j.jfx.exampleapp.model.ExampleModel;
import com.pi4j.jfx.util.mvc.ControllerBase;


public class ExampleController extends ControllerBase<ExampleModel> {

    private final LEDController ledController;
    private final CounterController counterController;

    public ExampleController(ExampleModel model) {
        super(model);
        ledController     = new LEDController(model);
        counterController = new CounterController(model);
    }

    // the logic we need in our application
    // these methods can be called from GUI and PUI (and nothing else)

    @Override
    public void shutdown() {
        super.shutdown();
        ledController.shutdown();
        counterController.shutdown();
    }

    public void increaseCounter() {
        counterController.increaseCounter();
    }

    public void decreaseCounter() {
        counterController.decreaseCounter();
    }

    public void setLedGlows(boolean glows){
        ledController.setLedGlows(glows);
    }

    public void blink(){
        ledController.blink();
    }
}
