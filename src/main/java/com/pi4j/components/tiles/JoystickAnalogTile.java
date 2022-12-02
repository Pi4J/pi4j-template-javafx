package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.JoystickAnalogInterface;
import com.pi4j.components.tiles.Skins.JoystickAnalogSkin;
import javafx.scene.input.KeyCode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class JoystickAnalogTile extends Pi4JTile implements JoystickAnalogInterface {

    private Consumer<Double> xOnMove;
    private Consumer<Double> yOnMove;

    private double xStart;
    private double yStart;

    private double currentX;
    private double currentY;

    private Runnable pushOnDown = () -> { };
    private Runnable pushOnUp   = () -> { };
    private Runnable pushWhilePressed = () -> { };

    private boolean isDown = false;
    private long whilePressedDelay;

    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);

            if(isDown) {
                pushWhilePressed.run();
            }
        }
    };

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    JoystickAnalogSkin jASkin = new JoystickAnalogSkin(this);

    private final double xMinNormValue = jASkin.getBorder().getCenterX() - jASkin.getBorder().getRadius()/2;
    private final double xMaxNormValue = jASkin.getBorder().getCenterX() + jASkin.getBorder().getRadius()/2;
    private final double yMinNormValue = jASkin.getBorder().getCenterY() - jASkin.getBorder().getRadius()/2;
    private final double yMaxNormValue = jASkin.getBorder().getCenterY() + jASkin.getBorder().getRadius()/2;

    public JoystickAnalogTile() {
        minHeight(400);
        minWidth(400);
        setTitle("JoystickAnalog");
        setText("Pin");
        setSkin(jASkin);

        jASkin.getButton().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - jASkin.getButton().getTranslateX();
            yStart = mouseEvent.getSceneY() - jASkin.getButton().getTranslateY();
        });

        jASkin.getButton().setOnMouseDragged(mouseEvent -> {
            jASkin.getButton().setTranslateX(mouseEvent.getSceneX() - xStart);
            jASkin.getButton().setTranslateY(mouseEvent.getSceneY() - yStart);
            currentX = mouseEvent.getSceneX() - xStart;
            currentY = mouseEvent.getSceneY() - yStart;
            xOnMove.accept(currentX);
            yOnMove.accept(currentY);
        });

        jASkin.getButton().setOnMouseReleased(mouseEvent -> {
            jASkin.getButton().setTranslateX(jASkin.getInfoRegion().getWidth() * 0.5);
            jASkin.getButton().setTranslateY(jASkin.getInfoRegion().getWidth() * 0.5);
            currentX = jASkin.getInfoRegion().getWidth() * 0.5;
            currentY = jASkin.getInfoRegion().getWidth() * 0.5;
            xOnMove.accept(currentX);
            yOnMove.accept(currentY);
        });

        //Run pushOnDown, when Key DOWN is pressed
        setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DOWN) {
                //Run onDown, when Value not Null
                if (pushOnDown != null && !isDown) {
                    pushOnDown.run();
                    isDown = true;
                }

                //Run whilePressedWorker, when Value not Null
                if (pushWhilePressed != null) {
                    executor.submit(whilePressedWorker);
                }
            }
        });

        //Run pushOnUp, when Key DOWN is released
        setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DOWN) {
                //Run onDown Runnable, falls Wert nicht Null
                if (pushOnUp != null && isDown) {
                    pushOnUp.run();
                    isDown = false;
                }
            }
        });
    }

    // Set current thread to sleep with given value (milliseconds)
    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void xOnMove(Consumer<Double> task) {
        xOnMove = task;

 //       if (value < xMinNormValue) value = xMinNormValue;
   //     if (value > xMaxNormValue) value = xMaxNormValue;
     //   task.accept(value);
    }

    @Override
    public void yOnMove(Consumer<Double> task) {
        yOnMove = task;

   //     if (value < yMinNormValue) value = yMinNormValue;
     //   if (value > yMaxNormValue) value = yMaxNormValue;
       // task.accept(value);
    }

    @Override
    public void pushOnDown(Runnable task) {
        pushOnDown = task;
    }

    @Override
    public void pushOnUp(Runnable task) {
        pushOnUp = task;
    }

    @Override
    public void pushWhilePressed(Runnable task, long whilePressedDelay) {
        pushWhilePressed = task;
        this.whilePressedDelay = whilePressedDelay;
    }

    @Override
    public void deregisterAll() {

    }

    @Override
    public void calibrateJoystick() {

    }

}
