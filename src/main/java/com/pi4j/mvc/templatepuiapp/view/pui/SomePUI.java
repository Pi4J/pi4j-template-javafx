package com.pi4j.mvc.templatepuiapp.view.pui;

import com.pi4j.catalog.components.SimpleButton;
import com.pi4j.catalog.components.SimpleLED;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.context.Context;
import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLED led;
    protected SimpleButton button;

    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLED(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        //always trigger a Controller action
        button.onDown(controller::ledOn);

        //don't call 'led.off()' here. You will miss the Controller logic (increase the terminationCounter and terminate)
        button.onUp(controller::ledOff);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        onChangeOf(model.ledGlows)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });
    }
}
