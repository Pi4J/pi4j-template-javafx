package com.pi4j.mvc.templatepuiapp.view.pui.components;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.mvc.util.puicomponentbase.Component;

/**
 * Example of a new component, not included in CrowPi-tutorial, using the same base classes and utilities.
 */
public class LEDComponent extends Component {

    /**
     * Pi4J digital output instance used by this component
     */
    protected final DigitalOutput digitalOutput;

    /**
     * Creates a new led component with custom GPIO addres.
     *
     * @param pi4j    Pi4J context
     * @param address GPIO address of led
     */
    public LEDComponent(Context pi4j, int address) {
        DigitalOutputConfigBuilder config = DigitalOutput.newConfigBuilder(pi4j)
                                                         .id("led-" + address)
                                                         .name("LED " + address)
                                                         .address(address)
                                                         .shutdown(DigitalState.LOW)
                                                         .initial(DigitalState.LOW);
        digitalOutput = pi4j.create(config);
    }

    public void on() {
        digitalOutput.high();
    }

    public void off() {
        digitalOutput.low();
    }

    public boolean glows(){
        return digitalOutput.isHigh();
    }

}
