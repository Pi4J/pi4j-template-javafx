package com.pi4j.jfx.exampleapp.pui;

import com.pi4j.context.Context;
import com.pi4j.jfx.exampleapp.model.ExamplePM;
import com.pi4j.jfx.exampleapp.pui.components.ButtonComponent;
import com.pi4j.jfx.exampleapp.pui.components.LEDComponent;
import com.pi4j.jfx.exampleapp.pui.util.PUI_Base;

public class ExamplePUI extends PUI_Base<ExamplePM> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected LEDComponent    led;
    protected ButtonComponent button;

    public ExamplePUI(ExamplePM model,Context pi4J) {
        super(model, pi4J);
    }

    @Override
    protected void initializeComponents(ExamplePM model, Context pi4J) {
        led    = new LEDComponent(pi4J, 22);
        button = new ButtonComponent(pi4J, 24);
    }

    @Override
    protected void setupInputEvents(ExamplePM model) {
        // don't forget to use 'withModel' you trigger some updates on model
        button.onUp(() -> withModel(() -> model.decreaseCounter()));
    }

    @Override
    protected void setupModelChangeListeners(ExamplePM model) {
        model.ledGlowsProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                led.on();
            } else {
                led.off();
            }
        });
    }
}
