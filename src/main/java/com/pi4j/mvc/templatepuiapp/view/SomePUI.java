package com.pi4j.mvc.templatepuiapp.view;

import com.pi4j.context.Context;
import com.pi4j.mvc.templatepuiapp.controller.SomeController;
import com.pi4j.mvc.templatepuiapp.model.SomeModel;
import com.pi4j.mvc.templatepuiapp.view.pui.components.ButtonComponent;
import com.pi4j.mvc.templatepuiapp.view.pui.components.LEDComponent;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected LEDComponent    led;
    protected ButtonComponent button;

    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new LEDComponent(pi4J, 22);
        button = new ButtonComponent(pi4J, 24);
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
