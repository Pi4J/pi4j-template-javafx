package com.pi4j.components.components;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;

public class SimpleLED extends Component implements com.pi4j.components.interfaces.SimpleLEDInterface {
    /**
     * Pi4J digital output instance used by this component
     *
     * It's 'package private' for testing reasons only
     */
    final DigitalOutput digitalOutput;

    /**
     * Creates a new simpleLed component with a custom BCM pin.
     *
     * @param pi4j    Pi4J context
     * @param address Custom BCM pin address
     */
    public SimpleLED(Context pi4j, PIN address) {
        this.digitalOutput = pi4j.create(buildDigitalOutputConfig(pi4j, address));
    }


    /**
     * Sets the LED to on.
     */
    @Override
    public void on() {
        digitalOutput.on();
    }

    /**
     * Sets the LED to off
     */
    @Override
    public void off() {
        digitalOutput.off();
    }

    /**
     * Toggle the LED state depending on its current state.
     *
     * @return Return true or false according to the new state of the relay.
     */
    @Override
    public boolean toggle() {
        digitalOutput.toggle();
        return digitalOutput.isOff();
    }

    @Override
    public boolean glows() {
        return !digitalOutput.isOff();
    }

    public int pin(){
        return digitalOutput.address().intValue();
    }

    /**
     * Configure Digital Output
     *
     * @param pi4j    PI4J Context
     * @param address GPIO Address of the relay
     * @return Return Digital Output configuration
     */
    private DigitalOutputConfig buildDigitalOutputConfig(Context pi4j, PIN address) {
        return DigitalOutput.newConfigBuilder(pi4j)
                .id("BCM" + address)
                .name("LED")
                .address(address.getPin())
                .build();
    }

}
