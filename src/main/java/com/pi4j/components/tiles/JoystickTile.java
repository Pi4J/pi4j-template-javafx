package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.JoystickInterface;
import com.pi4j.components.tiles.Skins.JoystickSkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoystickTile extends Pi4JTile implements JoystickInterface {

    JoystickSkin jSkin = new JoystickSkin(this);

    private boolean isDown  = false;
    private boolean isNorth = false;
    private boolean isSouth = false;
    private boolean isWest  = false;
    private boolean isEast  = false;
    private long millis;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Runnable onPushDown = () -> {};
    private Runnable onPushUp   = () -> {};
    private Runnable onNorth    = () -> {};
    private Runnable onSouth    = () -> {};
    private Runnable onWest     = () -> {};
    private Runnable onEast     = () -> {};

    private Runnable whileNorth    = () -> {};
    private Runnable whileSouth    = () -> {};
    private Runnable whileWest     = () -> {};
    private Runnable whileEast     = () -> {};

    private Runnable pushWhilePushed = () -> {};

    private final Runnable whilePressedWorker = () -> {

        while (isDown) {
            delay(millis);

            if(isDown) {
                pushWhilePushed.run();
            }
        }

        while (isNorth) {
            delay(millis);

            if(isNorth) {
                whileNorth.run();
            }
        }

        while (isSouth) {
            delay(millis);

            if(isSouth) {
                whileSouth.run();
            }
        }

        while (isWest) {
            delay(millis);

            if(isWest) {
                whileWest.run();
            }
        }

        while (isEast) {
            delay(millis);

            if(isEast) {
                whileEast.run();
            }
        }
    };

    public JoystickTile(){
        minHeight(400);
        minWidth(400);
        setTitle("Joystick");
        setText("");
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
            onPushUp.run();
            isDown = false;
        });
        jSkin.getButton().setOnMouseReleased(mouseEvent -> {
            onPushUp.run();
            isDown = false;
        });

        jSkin.getUp().setOnMousePressed(mouseEvent -> {

            //Run onNorth Runnable, if value =! Null
            if (onNorth != null) {
                onNorth.run();
                isNorth = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileNorth != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getDown().setOnMousePressed(mouseEvent -> {

            //Run onSouth Runnable, if value =! Null
            if (onSouth != null) {
                onSouth.run();
                isSouth = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileSouth != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getLeft().setOnMousePressed(mouseEvent -> {

            //Run onWest Runnable, if value =! Null
            if (onWest != null) {
                onWest.run();
                isWest = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileWest != null) {
                executor.submit(whilePressedWorker);
            }
        });

        jSkin.getRight().setOnMousePressed(mouseEvent -> {

            //Run onEast Runnable, if value =! Null
            if (onEast != null) {
                onEast.run();
                isEast = true;
            }

            //runs whilePressedWorker Runnable, if value =! Null
            if (whileEast != null) {
                executor.submit(whilePressedWorker);
            }
        });

        //set value to false, if releasing mouse
        jSkin.getUp().setOnMouseReleased(mouseEvent -> isNorth = false);
        jSkin.getDown().setOnMouseReleased(mouseEvent -> isSouth = false);
        jSkin.getLeft().setOnMouseReleased(mouseEvent -> isWest = false);
        jSkin.getRight().setOnMouseReleased(mouseEvent -> isEast = false);

        //set value to false, if moving mouse away from button area
        jSkin.getUp().setOnMouseExited(mouseEvent -> isNorth = false);
        jSkin.getDown().setOnMouseExited(mouseEvent -> isSouth = false);
        jSkin.getLeft().setOnMouseExited(mouseEvent -> isWest = false);
        jSkin.getRight().setOnMouseExited(mouseEvent -> isEast = false);



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
        this.whileNorth = method;
        this.millis = millis;

    }

    @Override
    public void onWest(Runnable handler) {
        onWest = handler;
    }

    @Override
    public void whileWest(long millis, Runnable method) {
        whileWest = method;
        this.millis = millis;

    }

    @Override
    public void onSouth(Runnable handler) {
        onSouth = handler;
    }

    @Override
    public void whileSouth(long millis, Runnable method) {
        this.whileSouth = method;
        this.millis = millis;

    }

    @Override
    public void onEast(Runnable handler) {
        onEast = handler;
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
}
