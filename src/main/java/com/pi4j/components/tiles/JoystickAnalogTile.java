package com.pi4j.components.tiles;

import com.pi4j.components.tiles.Skins.JoystickAnalogSkin;

public class JoystickAnalogTile extends Pi4JTile {

    JoystickAnalogSkin jASkin = new JoystickAnalogSkin(this);

    public JoystickAnalogTile() {
        minHeight(400);
        minWidth(400);
        setTitle("JoystickAnalog");
        setText("Pin");
        setSkin(jASkin);
    }
}
