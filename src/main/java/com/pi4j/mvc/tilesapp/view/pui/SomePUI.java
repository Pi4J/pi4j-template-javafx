package com.pi4j.mvc.tilesapp.view.pui;

import com.pi4j.components.components.Ads1115;
import com.pi4j.components.components.Joystick;
import com.pi4j.components.components.JoystickAnalog;
import com.pi4j.components.components.SimpleButton;
import com.pi4j.components.components.SimpleLED;
import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickAnalogInterface;
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

    protected JoystickAnalogInterface joystickAnalog;
    Ads1115 ads1115;

    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    @Override
    public void initializeParts() {
        led    = new SimpleLED(pi4J, PIN.D22);
        button = new SimpleButton(pi4J, PIN.D24, false);
        joystick = new Joystick(pi4J, PIN.D6, PIN.PWM13, PIN.PWM19, PIN.D26);
        ads1115 = new Ads1115(pi4J, 0x01, Ads1115.GAIN.GAIN_4_096V, Ads1115.ADDRESS.GND, 4);
        joystickAnalog = new JoystickAnalog(pi4J, ads1115, 0, 1, 3.3, true, PIN.D5);                joystickAnalog.start(0.005,10);
        joystickAnalog.start(0.05,10);
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

        joystickAnalog.xOnMove(controller::setX);
        joystickAnalog.yOnMove(controller::setY);

        joystickAnalog.pushOnDown(() -> controller.sendMessage("Joystick analog", true));
        joystickAnalog.pushOnUp  (() -> controller.sendMessage("Joystick analog",false));
        joystickAnalog.pushWhilePressed(() -> controller.whileMessage("Joystick analog"),3000);
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

        onChangeOf(model.currentXPosition)
            .execute((oldValue, newValue) -> System.out.println("X Position: " + newValue));

        onChangeOf(model.currentYPosition)
            .execute((oldValue, newValue) -> System.out.println("Y Position: " + newValue));
    }
}
