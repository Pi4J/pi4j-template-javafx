package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.tiles.Skins.JoystickSkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoystickTile extends Pi4JTile implements JoystickInterface {

    JoystickSkin jSkin = new JoystickSkin(this);

    private boolean isDown  = false;
    //    private boolean isNorth = false;
    //    private boolean isSouth = false;
    //    private boolean isWest  = false;
    //    private boolean isEast  = false;
    private long millis;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Runnable onPushDown = () -> {};
    private Runnable onPushUp   = () -> {};
    private Runnable onNorth    = () -> {};
    private Runnable onSouth    = () -> {};
    private Runnable onWest     = () -> {};
    private Runnable onEast     = () -> {};

    private Runnable pushWhilePushed = () -> {};

    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(millis);

            if(isDown) {
                pushWhilePushed.run();
            }
        }
    };

    public JoystickTile(){
        minHeight(400);
        minWidth(400);
        setTitle("Simple LED");
        setText("Pin");
        setSkin(jSkin);
        jSkin.getButton().setOnMousePressed(mouseEvent -> {

            //Run onPushDown Runnable, falls Wert nicht Null
            if (onPushDown != null) {
                onPushDown.run();
                isDown = true;
            }

            //Läuft whilePressedWorker Runnable, falls Wert nicht Null
            if (pushWhilePushed != null) {
                executor.submit(whilePressedWorker);
            }

        });
            jSkin.getButton().setOnMouseReleased(mouseEvent -> {
                onPushUp.run();
                isDown = false;
        });

        jSkin.getButton().setOnMouseReleased(mouseEvent -> onPushUp.run());

        jSkin.getUp().setOnMouseClicked(mouseEvent -> onNorth.run());
        jSkin.getDown().setOnMouseClicked(mouseEvent -> onSouth.run());
        jSkin.getLeft().setOnMouseClicked(mouseEvent -> onWest.run());
        jSkin.getRight().setOnMouseClicked(mouseEvent -> onEast.run());
    }

    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onNorth(Runnable handler) {
        onNorth = handler;
    }

    @Override
    public void whileNorth(long millis, Runnable method) {
        this.onNorth = method;
//        this.millis = millis;

    }

    @Override
    public void onWest(Runnable handler) {
        onWest = handler;
    }

    @Override
    public void whileWest(long millis, Runnable method) {
        this.onWest = method;
//        this.millis = millis;

    }

    @Override
    public void onSouth(Runnable handler) {
        onSouth = handler;
    }

    @Override
    public void whileSouth(long millis, Runnable method) {
        this.onSouth = method;
//        this.millis = millis;

    }

    @Override
    public void onEast(Runnable handler) {
        onEast = handler;
    }

    @Override
    public void whileEast(long millis, Runnable method) {
        this.onEast = method;
//        this.millis = millis;

    }

    @Override
    public void onPushDown(Runnable handler) {
        onPushDown = handler;

    }

    @Override
    public void onPushUp(Runnable method) {
        onPushUp = method;
    }

    @Override
    public void pushWhilePushed(long millis, Runnable method) {
        this.pushWhilePushed = method;
        this.millis = millis;

    }

    @Override
    public void deRegisterAll() {

    }
}
