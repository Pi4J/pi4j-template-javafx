package com.pi4j.jfx.templateapp.view.pui;

import com.pi4j.context.Context;
import com.pi4j.jfx.templateapp.controller.SomeController;
import com.pi4j.jfx.templateapp.model.SomeModel;
import com.pi4j.jfx.templateapp.view.pui.components.ButtonComponent;
import com.pi4j.jfx.templateapp.view.pui.components.LEDComponent;
import com.pi4j.jfx.util.mvc.PUI_Base;

public class SomePUI extends PUI_Base<SomeModel, SomeController> {
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
        button.onUp(controller::decreaseCounter);
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
