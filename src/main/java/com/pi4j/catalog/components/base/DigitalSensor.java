package com.pi4j.catalog.components.base;

import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

/**
 * Abstract class representing a digital sensor component in the system.
 * This class provides a foundation for components that utilize digital input functionality, such as buttons or switches.
 *
 * It integrates with Pi4J's `DigitalInput` API to interact with hardware-level digital GPIO pins.
 */
public abstract class DigitalSensor extends Component  {
    /**
     * Pi4J digital input instance used by this component (that's the low-level Pi4J Class)
     */
    protected final DigitalInput digitalInput;

    protected DigitalSensor(Context pi4j, DigitalInputConfig config){
        Objects.requireNonNull(pi4j);
        Objects.requireNonNull(config);

        digitalInput = pi4j.create(config);
    }

    public int pinNumber(){
        return digitalInput.bcm();
    }


    // --------------- for testing --------------------

    public MockDigitalInput mock() {
        return asMock(MockDigitalInput.class, digitalInput);
    }
}
