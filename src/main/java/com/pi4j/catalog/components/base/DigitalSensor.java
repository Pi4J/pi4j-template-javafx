package com.pi4j.catalog.components.base;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;

public abstract class DigitalSensor extends Component  {
    /**
     * Pi4J digital input instance used by this component (that's the low-level Pi4J Class)
     */
    protected final DigitalInput digitalInput;

    protected DigitalSensor(Context pi4j, DigitalInputConfig config){
        digitalInput = pi4j.create(config);
    }

    public int pinNumber(){
        return digitalInput.address().intValue();
    }


    // --------------- for testing --------------------

    public MockDigitalInput mock() {
        return asMock(MockDigitalInput.class, digitalInput);
    }
}
