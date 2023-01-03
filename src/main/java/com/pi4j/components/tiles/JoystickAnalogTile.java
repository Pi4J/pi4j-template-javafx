package com.pi4j.components.tiles;

import com.pi4j.components.components.Ads1115;
import com.pi4j.components.components.helpers.PIN;
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

    private double normX;

    private double normY;

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

    public JoystickAnalogTile(PIN pin, String ads_address) {
        minHeight(400);
        minWidth(400);
        setNormX(0.0);
        setNormY(0.0);
        setTitle("JoystickAnalog");
        setText("Pin "+pin.getPin()+", ADS: "+ads_address);
        setDescription("X/Y: ("+String.format("%.2f", getNormX())+"/"+String.format("%.2f", getNormY())+")");
        setSkin(jASkin);

        jASkin.getButton().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - jASkin.getButton().getTranslateX();
            yStart = mouseEvent.getSceneY() - jASkin.getButton().getTranslateY();
        });

        jASkin.getButton().setOnMouseDragged(mouseEvent -> {
            // Radius of the border
            double border = jASkin.getBorder().getRadius();

            if ( mouseEvent.getSceneX()-xStart < border
                && mouseEvent.getSceneX()-xStart > -border
                && mouseEvent.getSceneY()-yStart < border
                && mouseEvent.getSceneY()-yStart > -border) {
                jASkin.getButton().setTranslateX(mouseEvent.getSceneX() - xStart);
                jASkin.getButton().setTranslateY(mouseEvent.getSceneY() - yStart);
                currentX = mouseEvent.getSceneX() - xStart;
                currentY = mouseEvent.getSceneY() - yStart;
                xOnMove.accept(currentX);
                yOnMove.accept(currentY);
            }

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
        xOnMove = value -> {

            double xNormValue = jASkin.getBorder().getRadius();

            value = currentX;
            //scale axis from 0 to 1
                value = 1 / xNormValue * value;

            setNormX(value);
            updatePos();
            task.accept(value);

        };
    }

    @Override
    public void yOnMove(Consumer<Double> task) {
        yOnMove = value -> {

        double yNormValue = jASkin.getBorder().getRadius();

            //ToDo: accepted value is -0.0, should be 0.0
            value = currentY;
            //scale axis from 0 to 1
            value = -1 / yNormValue * value;

        setNormY(value);
        updatePos();

        task.accept(value);
    };
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

    @Override
    public void start(double threshold, int readFrequency) {}

    @Override
    public void stop(){}

    public double getNormX() {
        return normX;
    }

    public void setNormX(double normX) {
        this.normX = normX;
    }

    public double getNormY() {
        return normY;
    }

    public void setNormY(double normY) {
        this.normY = normY;
    }

    public void updatePos(){
        setDescription("X/Y: ("+String.format("%.2f", getNormX())+"/"+String.format("%.2f", getNormY())+")");
    }
}
