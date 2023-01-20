package com.pi4j.mvc.tilesapp.view.pui;

import com.pi4j.components.components.*;
import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.*;
import com.pi4j.context.Context;
import com.pi4j.mvc.tilesapp.controller.SomeController;
import com.pi4j.mvc.tilesapp.model.SomeModel;
import com.pi4j.mvc.util.mvcbase.PuiBase;

public class SomePUI extends PuiBase<SomeModel, SomeController> {

    //declare all hardware components attached to RaspPi
    //these are protected to give unit tests access to them
    protected SimpleLEDInterface led;
    protected SimpleButtonInterface button;
    protected LEDButtonInterface ledButton;
    protected JoystickInterface joystick;

    protected JoystickAnalogInterface joystickAnalog;
    Ads1115 ads1115;

    protected PotentiometerInterface potentiometer;

    private final int DEFAULT_SPI_CHANNEL = 0;


    public SomePUI(SomeController controller, Context pi4J) {
        super(controller, pi4J);
    }

    // Initialize all hardware components attached to RaspPi
    @Override
    public void initializeParts() {
        led    = new SimpleLED(pi4J, PIN.D17);
        button = new SimpleButton(pi4J, PIN.D24, false);
        ledButton = new LEDButton(pi4J,PIN.PWM18,false,PIN.D22);
        joystick = new Joystick(pi4J, PIN.D5, PIN.D6, PIN.PWM13, PIN.PWM19);
        ads1115 = new Ads1115(pi4J, 0x01, Ads1115.GAIN.GAIN_4_096V, Ads1115.ADDRESS.GND, 4);
        potentiometer = new Potentiometer(ads1115, DEFAULT_SPI_CHANNEL, 3.3);
        potentiometer.startSlowContinuousReading(0.05,10);
        joystickAnalog = new JoystickAnalog(pi4J, ads1115, 1, 2, 3.3, false, PIN.D26);
        joystickAnalog.start(0.05,10);
    }

    // All EventHandler from components which are needed
    @Override
    public void setupUiToActionBindings(SomeController controller) {

        button.onDown(() -> {
            // Sends button state boolean false
            controller.setButtonPressed(true);
            // Sends the current normalized potentiometer position
            controller.singlePotentiometer(potentiometer.singleShotGetNormalizedValue());

        });
        // Sends component name and button state boolean false
        button.onUp  (() -> controller.setButtonPressed(false));
        // Sends component name and amount of delay in milliseconds
        button.whilePressed(() -> controller.whileMessage("Simple"),5000);

        // Sends message to controller which direction and if short or long press
        joystick.onNorthUp(() -> controller.sendMessage("Up",false));
        joystick.whileNorth(2000, () -> controller.whileMessage("Up"));
        joystick.onSouthUp(() -> controller.sendMessage("Down",false));
        joystick.whileSouth(2000, () -> controller.whileMessage("Down"));
        joystick.onWestUp(() -> controller.sendMessage("Left", false));
        joystick.whileWest(2000, () -> controller.whileMessage("Left"));
        joystick.onEastUp(() -> controller.sendMessage("Right",false));
        joystick.whileEast(2000, () -> controller.whileMessage("Right"));

        // Sends component name and button state boolean true
        ledButton.onDown(() -> controller.sendMessage("LED",true));
        // Sends  button state boolean false
        ledButton.onUp(controller::setLedButtonReleased);
        // Sends button state boolean true for the amount of delay in milliseconds
        ledButton.btnwhilePressed(controller::whilePressedLedButton, 1000);

//        joystickAnalog.xOnMove(controller::setJoyAnalogX);
//        joystickAnalog.yOnMove(controller::setJoyAnalogY);
//
//        // Sends component name and button state boolean true
//        joystickAnalog.pushOnDown(() -> controller.sendMessage("Joystick analog", true));
//
//        // Sends component name and button state boolean false
//        joystickAnalog.pushOnUp  (() -> controller.sendMessage("Joystick analog",false));
//
//        // Sends component name and amount of delay in milliseconds
//        joystickAnalog.pushWhilePressed(() -> controller.whileMessage("Joystick analog"),3000);

        // Provide Consumer with normalized position of Potentiometer
        potentiometer.setConsumerSlowReadChan(controller::setPotiX);
    }

    @Override
    public void setupModelToUiBindings(SomeModel model) {

        // Observes isButtonPressed ObservableValue
        // Press button to turn on/off SimpleLED
        onChangeOf(model.isButtonPressed)
                .execute((oldValue, newValue) -> {
                    if (newValue) {
                        led.on();
                    } else {
                        led.off();
                    }
                });
    }
}
