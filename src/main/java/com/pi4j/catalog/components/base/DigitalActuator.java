package com.pi4j.catalog.components.base;

import java.util.Arrays;
import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;

/**
 * An abstract class that represents a digital actuator device.
 * It provides a foundational implementation for components that interact with a digital output pin.
 *
 * An example for a digital actuator is a LED.
 *
 * It integrates with Pi4J's `DigitalOutput` API to interact with hardware-level digital GPIO pins.
 *
 * Classes extending DigitalActuator are expected to define specific behaviors for their respective devices.
 */
public abstract class DigitalActuator extends Component {
    /**
     * Pi4J digital output instance used by this component
     */
    protected final DigitalOutput digitalOutput;

    protected DigitalActuator(Context pi4j, DigitalOutputConfig config) {
        Objects.requireNonNull(pi4j);
        Objects.requireNonNull(config);

        digitalOutput = pi4j.create(config);
    }

    public int pinNumber(){
        return digitalOutput.bcm();
    }


    // --------------- for testing --------------------

    public MockDigitalOutput mock() {
        return asMock(MockDigitalOutput.class, digitalOutput);
    }

    public MockDigitalOutput[] mock(DigitalOutput[] digitalOutputs) {
        return Arrays.stream(digitalOutputs)
                .map(d -> asMock(MockDigitalOutput.class, d))
                .toArray(MockDigitalOutput[]::new);
    }

}
