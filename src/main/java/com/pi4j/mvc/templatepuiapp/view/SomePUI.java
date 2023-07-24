package com.pi4j.mvc.templatepuiapp.view;

import com.pi4j.context.Context;


import com.pi4j.catalog.components.SimpleButton;
import com.pi4j.catalog.components.SimpleLed;
import com.pi4j.catalog.components.base.PIN;

import com.pi4j.mvc.util.mvcbase.PuiBase;

import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;


public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLed led;
    protected SimpleButton button;

    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLed(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        //if user interacts with one of the parts, always trigger a Controller action
        button.onDown(controller::activate);

        //don't call 'led.off()' here. You will miss the Controller logic (increase the terminationCounter and terminate)
        button.onUp(controller::deactivate);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
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
