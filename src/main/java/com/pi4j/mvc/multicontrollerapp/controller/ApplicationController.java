package com.pi4j.mvc.multicontrollerapp.controller;

import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.mvcbase.ControllerBase;

/**
 * Provides all the available actions to the UI.
 *
 * Usually all the methods just delegate the call to the appropriate (Sub-)Controller.
 *
 */
public class ApplicationController extends ControllerBase<ExampleModel> {

    private final LEDController     ledController;
    private final CounterController counterController;

    public ApplicationController(ExampleModel model) {
        super(model);
        ledController     = new LEDController(model);
        counterController = new CounterController(model);
    }

    @Override
    public void awaitCompletion() {
        super.awaitCompletion();
        ledController.awaitCompletion();
        counterController.awaitCompletion();
    }

    // the actions we need in our application
    // these methods are public and can be called from GUI and PUI (and nothing else)

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
