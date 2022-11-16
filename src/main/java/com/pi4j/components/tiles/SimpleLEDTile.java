package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.SimpleLEDInterface;

public class SimpleLEDTile extends Pi4JTile implements SimpleLEDInterface {

    public SimpleLEDTile(PIN pin) {
        minHeight(400);
        minWidth(400);
        setSkinType(SkinType.LED);
        setTitle("Simple LED");
        setText("Pin " + pin.getPin());
    }

    @Override
    public void on() {
        this.setActive(true);
    }

    @Override
    public void off() {
        this.setActive(false);
    }

}
