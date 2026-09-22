package com.pi4j.mvc.multicontrollerapp.view.pui;

import com.pi4j.mvc.multicontrollerapp.controller.ApplicationController;
import com.pi4j.mvc.multicontrollerapp.model.ExampleModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

import com.pi4j.catalog.components.base.PIN;
import com.pi4j.catalog.components.button.GpioButton;
import com.pi4j.catalog.components.led.GpioLed;

public class ExamplePUI extends PuiBase<ExampleModel, ApplicationController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected GpioLed led;
    protected GpioButton button;

    public ExamplePUI(ApplicationController controller) {
        super(controller);
    }

    @Override
    public void initializeComponents() {
        led    = new GpioLed(pi4J, PIN.D22);
        button = new GpioButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupEventHandler(ApplicationController controller) {
        button.onUp(controller::decreaseCounter);
    }

    @Override
    public void updateComponents(ExampleModel model) {
        onChangeOf(model.isActive)
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
