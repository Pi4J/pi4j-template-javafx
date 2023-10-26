package com.pi4j.catalog.components.base;

import java.util.Arrays;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;

public abstract class DigitalActuator extends Component {
    /**
     * Pi4J digital output instance used by this component
     */
    protected final DigitalOutput digitalOutput;

    protected DigitalActuator(Context pi4j, DigitalOutputConfig config) {
        digitalOutput = pi4j.create(config);
    }

    public int pinNumber(){
        return digitalOutput.address().intValue();
    }


    // --------------- for testing --------------------

    public MockDigitalOutput mock() {
        return asMock(MockDigitalOutput.class, digitalOutput);
    }

    public MockDigitalOutput[] mock(DigitalOutput[] digitalOutputs) {
        return Arrays.stream(digitalOutputs)
                .map(d -> asMock(MockDigitalOutput.class, digitalOutput))
                .toArray(MockDigitalOutput[]::new);
    }

}
