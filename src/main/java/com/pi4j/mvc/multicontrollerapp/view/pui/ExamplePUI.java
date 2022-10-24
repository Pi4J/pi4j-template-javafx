package com.pi4j.mvc.multicontrollerapp.view.pui;

import com.pi4j.catalog.components.SimpleButton;
import com.pi4j.catalog.components.SimpleLED;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.context.Context;
import com.pi4j.mvc.multicontrollerapp.controller.ApplicationController;
import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class ExamplePUI extends PuiBase<ExampleModel, ApplicationController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLED    led;
    protected SimpleButton button;

    public ExamplePUI(ApplicationController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLED(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupUiToActionBindings(ApplicationController controller) {
        button.onUp(controller::decreaseCounter);
    }

    @Override
    public void setupModelToUiBindings(ExampleModel model) {
        onChangeOf(model.ledGlows)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });

        // if you want to use the built-in blinking feature (instead of implementing blinking in Controller):
//        onChangeOf(model.blinkingTrigger)
//                .execute((oldValue, newValue) -> led.blink(4, Duration.ofMillis(500)));

    }
}
