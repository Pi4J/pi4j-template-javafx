package com.pi4j.jfx.exampleapp.view.pui;

import java.time.Duration;

import com.pi4j.context.Context;
import com.pi4j.jfx.exampleapp.controller.ExampleController;
import com.pi4j.jfx.exampleapp.model.ExampleModel;
import com.pi4j.jfx.exampleapp.view.pui.components.ButtonComponent;
import com.pi4j.jfx.exampleapp.view.pui.components.LEDComponent;
import com.pi4j.jfx.util.mvc.PUI_Base;

public class ExamplePUI extends PUI_Base<ExampleModel, ExampleController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected LEDComponent    led;
    protected ButtonComponent button;

    public ExamplePUI(ExampleController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new LEDComponent(pi4J, 22);
        button = new ButtonComponent(pi4J, 24);
    }

    @Override
    public void setupUiToActionBindings(ExampleController controller) {
        button.onUp(controller::decreaseCounter);
    }

    @Override
    public void setupModelToUiBindings(ExampleModel model) {
        onChangeOf(model.ledGlows)
                .triggerPUIAction((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });

        onChangeOf(model.blinkingTrigger)
                .triggerPUIAction((oldValue, newValue) -> led.blink(4, Duration.ofMillis(500)));

    }
}
