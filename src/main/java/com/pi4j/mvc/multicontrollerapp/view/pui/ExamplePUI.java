package com.pi4j.mvc.multicontrollerapp.view.pui;

import com.pi4j.context.Context;
import com.pi4j.mvc.multicontrollerapp.controller.ApplicationController;
import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.multicontrollerapp.view.pui.components.ButtonComponent;
import com.pi4j.mvc.multicontrollerapp.view.pui.components.LEDComponent;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class ExamplePUI extends PuiBase<ExampleModel, ApplicationController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected LEDComponent    led;
    protected ButtonComponent button;

    public ExamplePUI(ApplicationController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new LEDComponent(pi4J, 22);
        button = new ButtonComponent(pi4J, 24);
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
