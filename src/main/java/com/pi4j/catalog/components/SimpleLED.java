package com.pi4j.catalog.components;

import com.pi4j.context.Context;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;

public class SimpleLED extends Component {
    /**
     * Pi4J digital output instance used by this component
     */
    private final DigitalOutput digitalOutput;

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
     * Set the LED on or off depending on the boolean argument.
     *
     * @param on Sets the LED to on (true) or off (false)
     */
    public void setState(boolean on) {
        digitalOutput.setState(on);
    }

    /**
     * Sets the LED to on.
     */
    public void on() {
        digitalOutput.on();
    }

    /**
     * Sets the LED to off
     */
    public void off() {
        digitalOutput.off();
    }

    /**
     * Toggle the LED state depending on its current state.
     *
     * @return Return true or false according to the new state of the relay.
     */
    public boolean toggleState() {
        digitalOutput.toggle();
        return digitalOutput.isOff();
    }

    /**
     * Returns the instance of the digital output
     *
     * @return DigitalOutput instance of the LED
     */
    public DigitalOutput getDigitalOutput() {
        return digitalOutput;
    }

    /**
     * Configure Digital Output
     *
     * @param pi4j    PI4J Context
     * @param address GPIO Address of the relay
     * @return Return Digital Output configuration
     */
    protected DigitalOutputConfig buildDigitalOutputConfig(Context pi4j, PIN address) {
        return DigitalOutput.newConfigBuilder(pi4j)
                .id("BCM" + address)
                .name("LED")
                .address(address.getPin())
                .build();
    }
}
