package com.pi4j.catalog.components.led;

import java.util.Objects;

import com.pi4j.io.gpio.digital.DigitalState;

import com.pi4j.catalog.components.base.Component;
import com.pi4j.catalog.components.Mcp23017;

public class Mcp23017Led extends Component implements Led {

    private final Mcp23017 mcp23017;
    private final Mcp23017.ExpanderPin expanderPin;
    private boolean on;

    public Mcp23017Led(Mcp23017 mcp23017, Mcp23017.ExpanderPin expanderPin) {
        this.mcp23017 = mcp23017;
        this.expanderPin = expanderPin;
        Objects.requireNonNull(mcp23017);
        Objects.requireNonNull(expanderPin);
        mcp23017.configure(expanderPin, Mcp23017.PinOptions.output());

        on = false;
        mcp23017.digitalWrite(expanderPin, DigitalState.LOW);
    }

    @Override
    public void on(){
        if(!isOn()){
            on = true;
            mcp23017.digitalWrite(expanderPin, DigitalState.HIGH);
        }
    }

    @Override
    public boolean isOn(){
        return on;
    }

    @Override
    public void off(){
        if(isOn()){
            on = false;
            mcp23017.digitalWrite(expanderPin, DigitalState.LOW);
        }
    }

    @Override
    public boolean toggle(){
        if(isOn()){
            off();
        }
        else {
            on();
        }
        return isOn();
    }

    @Override
    public void shutdown() {
        off();
        super.shutdown();
    }
}
