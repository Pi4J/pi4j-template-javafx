package com.pi4j.mvc.templatepuiapp.view;

import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

import com.pi4j.catalog.components.base.PIN;
import com.pi4j.catalog.components.button.Button;
import com.pi4j.catalog.components.button.GpioButton;
import com.pi4j.catalog.components.led.GpioLed;
import com.pi4j.catalog.components.led.Led;


public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected Led led;
    protected Button button;

    public SomePUI(SomeController controller) {
        super(controller);
    }

    @Override
    public void initializeComponents() {
        led    = new GpioLed(pi4J, PIN.D22);
        button = new GpioButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupEventHandler(SomeController controller) {
        //if the user interacts with one of the parts, always trigger a Controller action
        button.onDown(controller::activate);

        //don't call 'led.off()' here. You will miss the Controller logic (increase the terminationCounter and terminate)
        button.onUp(controller::deactivate);
    }

    @Override
    public void updateComponents(SomeModel model) {
        onChangeOf(model.busy)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });
    }
}
