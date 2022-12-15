package com.pi4j.mvc.tilesapp.view.pui;

import com.pi4j.components.components.Joystick;
import com.pi4j.components.components.SimpleButton;
import com.pi4j.components.components.SimpleLED;
import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.interfaces.SimpleLEDInterface;
import com.pi4j.context.Context;
import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class SomePUI extends PuiBase<SomeModel, SomeController> {
    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLEDInterface led;
    protected SimpleButtonInterface button;
    protected JoystickInterface joystick;

    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLED(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
        joystick = new Joystick(pi4J, PIN.D6, PIN.PWM13, PIN.PWM19, PIN.D26);
    }

    @Override
    public void setupUiToActionBindings(SomeController controller) {

        joystick.onWestDown(() -> controller.sendMessage("Left Joystick",true));
        joystick.onNorthDown(() -> controller.sendMessage("Up Joystick",true));
        joystick.onEastDown(() -> controller.sendMessage("Right Joystick",true));
        joystick.onSouthDown(() -> controller.sendMessage("Down Joystick",true));

        joystick.onWestUp(() -> controller.sendMessage("Left Joystick",false));
        joystick.onNorthUp(() -> controller.sendMessage("Up Joystick",false));
        joystick.onEastUp(() -> controller.sendMessage("Right Joystick",false));
        joystick.onSouthUp(() -> controller.sendMessage("Down Joystick",false));

        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {
        onChangeOf(model.isLedActive)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });
    }
}
