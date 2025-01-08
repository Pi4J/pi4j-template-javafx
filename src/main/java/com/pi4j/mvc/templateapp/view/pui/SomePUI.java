package com.pi4j.mvc.templateapp.view.pui;

import com.pi4j.context.Context;


import com.pi4j.catalog.components.SimpleButton;
import com.pi4j.catalog.components.SimpleLed;
import com.pi4j.catalog.components.base.PIN;

import com.pi4j.mvc.templateapp.controller.SomeController;
import com.pi4j.mvc.templateapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLed led;
    protected SimpleButton button;

    public SomePUI(SomeController controller) {
        super(controller);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLed(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {
        button.onDown(() -> controller.setIsActive(true));
        button.onUp  (() -> controller.decreaseCounter());
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        onChangeOf(model.isActive)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });
    }
}
