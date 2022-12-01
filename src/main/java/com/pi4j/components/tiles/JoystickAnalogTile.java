package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.JoystickAnalogInterface;
import com.pi4j.components.tiles.Skins.JoystickAnalogSkin;

import java.util.function.Consumer;

public class JoystickAnalogTile extends Pi4JTile implements JoystickAnalogInterface {

    private Consumer<Double> xOnMove;
    private Consumer<Double> yOnMove;

    private double xOffset = 0.0;
    private double yOffset = 0.0;
    private double xMinNormValue;
    private double xMaxNormValue;
    private double yMinNormValue;
    private double yMaxNormValue;


    JoystickAnalogSkin jASkin = new JoystickAnalogSkin(this);

    public JoystickAnalogTile() {
        minHeight(400);
        minWidth(400);
        setTitle("JoystickAnalog");
        setText("Pin");
        setSkin(jASkin);

//        jASkin.getButton().setOnMouseDragged(mouseEvent -> {
  //          xOnMove.accept();
    //    });
    }

    @Override
    public void xOnMove(Consumer<Double> task) {
        xOnMove = task;
    }

    @Override
    public void yOnMove(Consumer<Double> task) {

    }

    @Override
    public void pushOnDown(Runnable task) {

    }

    @Override
    public void pushOnUp(Runnable task) {

    }

    @Override
    public void pushWhilePressed(Runnable task, long whilePressedDelay) {

    }

    @Override
    public void start(double threshold, int readFrequency) {

    }

    @Override
    public void deregisterAll() {

    }

    @Override
    public void calibrateJoystick() {

    }
}
