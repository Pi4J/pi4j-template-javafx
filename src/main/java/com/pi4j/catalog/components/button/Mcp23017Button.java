package com.pi4j.catalog.components.button;

import java.util.Objects;

import com.pi4j.catalog.components.base.Component;
import com.pi4j.catalog.components.Mcp23017;

public class Mcp23017Button extends Component implements Button {
    private volatile boolean pressed = false;

    private final ButtonEventHelper eventHelper;

    public Mcp23017Button(Mcp23017 mcp23017, Mcp23017.ExpanderPin expanderPin){
        Objects.requireNonNull(mcp23017);
        Objects.requireNonNull(expanderPin);

        eventHelper = new ButtonEventHelper(this);

        mcp23017.configure(expanderPin, Mcp23017.PinOptions.input()
                                                           .pullUp(true)
                                                           .interruptOnChange());


        mcp23017.addListener(expanderPin, (pin, state) ->  {
            switch (state) {
                case LOW  -> pressed = true;
                case HIGH -> pressed = false;
                case UNKNOWN -> logError("Button is in State UNKNOWN");
            }
            eventHelper.onStateChange(state);
        });
    }

    @Override
    public ButtonEventHelper getEventHelper() {
        return eventHelper;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    public boolean hasPullUpResistor() {
        return true;
    }

    @Override
    public void shutdown() {
        pressed = false;
        eventHelper.shutdown();

        super.shutdown();
    }

}
