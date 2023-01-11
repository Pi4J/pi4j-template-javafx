package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.SimpleLEDInterface;
import com.pi4j.context.Context;

public class SimpleLEDTile extends Pi4JTile implements SimpleLEDInterface {

    public SimpleLEDTile(Context pi4j, PIN pin) {
        minHeight(400);
        minWidth(400);
        setSkinType(SkinType.LED);
        setTitle("Simple LED");
        setText("Pin " + pin.getPin());
    }

    /**
     * Sets the LED to on.
     */
    @Override
    public void on() {
        this.setActive(true);
    }

    /**
     * Sets the LED to off
     */
    @Override
    public void off() {
        this.setActive(false);
    }

}
