package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.tiles.Skins.JoystickSkin;

public class JoystickTile extends Pi4JTile implements JoystickInterface {

    public JoystickTile(){
        minHeight(400);
        minWidth(400);
        setTitle("Simple LED");
        setText("Pin");
        setSkin(new JoystickSkin(this));
    }




    @Override
    public void onNorth(Runnable handler) {

    }

    @Override
    public void whileNorth(long millis, Runnable method) {

    }

    @Override
    public void onWest(Runnable handler) {

    }

    @Override
    public void whileWest(long millis, Runnable method) {

    }

    @Override
    public void onSouth(Runnable handler) {

    }

    @Override
    public void whileSouth(long millis, Runnable method) {

    }

    @Override
    public void onEast(Runnable handler) {

    }

    @Override
    public void whileEast(long millis, Runnable method) {

    }

    @Override
    public void onPushDown(Runnable handler) {

    }

    @Override
    public void onPushUp(Runnable method) {

    }

    @Override
    public void pushWhilePushed(long millis, Runnable method) {

    }

    @Override
    public void deRegisterAll() {

    }
}
