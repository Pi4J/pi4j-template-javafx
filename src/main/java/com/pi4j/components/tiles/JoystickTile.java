package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.tiles.Skins.JoystickSkin;
import com.pi4j.context.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoystickTile extends Pi4JTile implements JoystickInterface {

    protected static final long DEFAULT_DEBOUNCE = 10000;

    private boolean isDown  = false;
    private boolean isNorth = false;
    private boolean isSouth = false;
    private boolean isWest  = false;
    private boolean isEast  = false;
    private long millis;

    private boolean isButtonActive;
    JoystickSkin jSkin = new JoystickSkin(this);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Runnable onPushDown = () -> {};
    private Runnable onPushUp   = () -> {};
    private Runnable onNorthDown = () -> {};
    private Runnable onSouthDown = () -> {};
    private Runnable onWestDown = () -> {};
    private Runnable onEastDown = () -> {};

    private Runnable onNorthUp    = () -> {};
    private Runnable onSouthUp    = () -> {};
    private Runnable onWestUp     = () -> {};
    private Runnable onEastUp     = () -> {};

    private Runnable whileNorth    = () -> {};
    private Runnable whileSouth    = () -> {};
    private Runnable whileWest     = () -> {};
    private Runnable whileEast     = () -> {};

    private Runnable pushWhilePushed = () -> {};

    private final Runnable whilePressedWorker = () -> {

        while (isDown || isNorth || isSouth || isWest || isEast) {
            delay(millis);
            if (isDown) pushWhilePushed.run();
            if (isNorth) whileNorth.run();
            if (isSouth) whileSouth.run();
            if (isWest) whileWest.run();
            if (isEast) whileEast.run();
        }
    };

    public JoystickTile(Context pi4J,PIN pin1, PIN pin2, PIN pin3, PIN pin4){
        constructorValues();
        setText("Pin "+pin1.getPin()+"↑, "+pin2.getPin()+"→, "+pin3.getPin()+"↓, "+pin4.getPin()+"←");
        isButtonActive = false;

    }

    public JoystickTile(Context pi4J,PIN pin1, PIN pin2, PIN pin3, PIN pin4, PIN pin5){
        constructorValues();
        setText("Pin "+pin1.getPin()+"↑, "+pin2.getPin()+"→, "+pin3.getPin()+"↓, "+pin4.getPin() +"←, push:"+pin5.getPin());
        isButtonActive = true;
    }

    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onNorthDown(Runnable handler) {
        onNorthDown = handler;
    }

    @Override
    public void onNorthUp(Runnable handler) {
        onNorthUp = handler;

    }

    @Override
    public void whileNorth(long millis, Runnable method) {
        this.whileNorth = method;
        this.millis = millis;

    }

    @Override
    public void onWestDown(Runnable handler) {
        onWestDown = handler;
    }

    @Override
    public void onWestUp(Runnable handler) {
        onWestUp = handler;
    }

    @Override
    public void whileWest(long millis, Runnable method) {
        whileWest = method;
        this.millis = millis;

    }

    @Override
    public void onSouthDown(Runnable handler) {
        onSouthDown = handler;
    }

    @Override
    public void onSouthUp(Runnable handler) {
        onSouthUp = handler;
    }

    @Override
    public void whileSouth(long millis, Runnable method) {
        this.whileSouth = method;
        this.millis = millis;

    }

    @Override
    public void onEastDown(Runnable handler) {
        onEastDown = handler;
    }

    @Override
    public void onEastUp(Runnable handler) {
        onEastUp = handler;
    }

    @Override
    public void whileEast(long millis, Runnable method) {
        this.whileEast = method;
        this.millis = millis;

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

    public void constructorValues(){

        minHeight(400);
        minWidth(400);
        setTitle("Joystick");
        setSkin(jSkin);

        jSkin.getButton().setOnMousePressed(mouseEvent -> {

            //Run onPushDown Runnable, if value =! Null
            if (onPushDown != null) {
                onPushDown.run();
                isDown = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (pushWhilePushed != null) {
                executor.submit(whilePressedWorker);
            }

        });

        jSkin.getButton().setOnMouseExited(mouseEvent -> {
            if(isDown) {
                onPushUp.run();
                isDown = false;
            }
        });

        jSkin.getButton().setOnMouseReleased(mouseEvent -> {
            if(isDown) {
                onPushUp.run();
                isDown = false;
            }
        });

        jSkin.getUp().setOnMousePressed(mouseEvent -> {

            //Run onNorth Runnable, if value =! Null
            if (onNorthDown != null) {
                onNorthDown.run();
                isNorth = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileNorth != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getDown().setOnMousePressed(mouseEvent -> {

            //Run onSouth Runnable, if value =! Null
            if (onSouthDown != null) {
                onSouthDown.run();
                isSouth = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileSouth != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getLeft().setOnMousePressed(mouseEvent -> {

            //Run onWest Runnable, if value =! Null
            if (onWestDown != null) {
                onWestDown.run();
                isWest = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileWest != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getRight().setOnMousePressed(mouseEvent -> {

            //Run onEast Runnable, if value =! Null
            if (onEastDown != null) {
                onEastDown.run();
                isEast = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileEast != null) {
                executor.submit(whilePressedWorker);
            }
        });

        //set value to false, if releasing mouse
        jSkin.getUp().setOnMouseReleased(mouseEvent -> {
            if(isNorth) {
                onNorthUp.run();
                isNorth = false;
            }
        });

        jSkin.getDown().setOnMouseReleased(mouseEvent -> {
            if(isSouth) {
                onSouthUp.run();
                isSouth = false;
            }
        });

        jSkin.getLeft().setOnMouseReleased(mouseEvent -> {
            if (isWest) {
                onWestUp.run();
                isWest = false;
            }
        });

        jSkin.getRight().setOnMouseReleased(mouseEvent -> {
            if (isWest) {
                onWestUp.run();
                isWest = false;
            }
        });

        //set value to false, if moving mouse away from button area

        jSkin.getUp().setOnMouseExited(mouseEvent -> {
            if(isNorth) {
                onNorthUp.run();
                isNorth = false;
            }
        });

        jSkin.getDown().setOnMouseExited(mouseEvent -> {
            if(isSouth) {
                onSouthUp.run();
                isSouth = false;
            }
        });

        jSkin.getLeft().setOnMouseExited(mouseEvent -> {
            if (isWest) {
                onWestUp.run();
                isWest = false;
            }
        });

        jSkin.getRight().setOnMouseExited(mouseEvent ->   {
            if(isEast) {
                onEastUp.run();
                isEast = false;
            }
        });
    }
}
