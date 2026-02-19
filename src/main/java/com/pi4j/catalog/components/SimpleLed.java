package com.pi4j.catalog.components;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

import com.pi4j.catalog.components.base.DigitalActuator;
import com.pi4j.catalog.components.base.PIN;

/**
 * Represents a simple LED component controlled through a digital GPIO pin.
 *
 * This class provides methods to turn the LED on, off, and toggle its state.
 *
 * It extends the {@link DigitalActuator} class to utilize the base functionality
 * for interacting with digital output pins.
 */
public class SimpleLed extends DigitalActuator {

    /**
     * Creates a new SimpleLed component with a custom BCM pin.
     *
     * @param pi4j    Pi4J context
     * @param address Custom BCM pin address
     */
    public SimpleLed(Context pi4j, PIN address) {
        super(pi4j,
              DigitalOutput.newConfigBuilder(pi4j)
                      .id("BCM" + address)
                      .name("LED #" + address)
                      .bcm(address.getPin())
                      .build());
        logDebug("Created new SimpleLed component on pin %s", address);
        digitalOutput.off();
    }

    /**
     * Sets LED to on.
     */
    public void on() {
        if(!isOn()){
            logDebug("LED turned ON");
            digitalOutput.on();
        }
    }

    public boolean isOn(){
        return digitalOutput.isOn();
    }

    /**
     * Sets LED to off
     */
    public void off() {
        if(isOn()){
            logDebug("LED turned OFF");
            digitalOutput.off();
        }
    }

    /**
     * Toggle the LED state depending on its current state.
     *
     * @return Return true or false according to the new state of the relay.
     */
    public boolean toggle() {
        digitalOutput.toggle();
        logDebug("LED toggled, now it is %s", digitalOutput.isOff() ? "OFF" : "ON");

        return digitalOutput.isOff();
    }

    @Override
    public void shutdown() {
        off();
        super.shutdown();
    }
}
