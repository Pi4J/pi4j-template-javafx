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

    private final double NORMALIZED_CENTER_POSITION = 0.5;

    private boolean normalized0to1 = true;

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

    private double xMinNormValue = jASkin.getBorder().getCenterX() - jASkin.getBorder().getRadius();
    private double xMaxNormValue = jASkin.getBorder().getCenterX() + jASkin.getBorder().getRadius();
    private double yMinNormValue = jASkin.getBorder().getCenterY() - jASkin.getBorder().getRadius();
    private double yMaxNormValue = jASkin.getBorder().getCenterY() + jASkin.getBorder().getRadius();

    public JoystickAnalogTile(PIN pin, String ads_address) {
        minHeight(400);
        minWidth(400);
        setNormX(0.0);
        setNormY(0.0);
        setTitle("JoystickAnalog");
        setText("Pin "+pin.getPin()+", ADS: "+ads_address); //TODO: AD Wandler PIN, etc..
        setDescription("X/Y: ("+String.format("%.2f", getNormX())+"/"+String.format("%.2f", getNormY())+")");
        setSkin(jASkin);

        jASkin.getButton().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - jASkin.getButton().getTranslateX();
            yStart = mouseEvent.getSceneY() - jASkin.getButton().getTranslateY();
        });

        jASkin.getButton().setOnMouseDragged(mouseEvent -> {
            double border = jASkin.getBorder().getRadius();

            if ( mouseEvent.getSceneX()-xStart < border
                && mouseEvent.getSceneX()-xStart > -border
                && mouseEvent.getSceneY()-yStart < border
                && mouseEvent.getSceneY()-yStart > -border){
                jASkin.getButton().setTranslateX(mouseEvent.getSceneX() - xStart);
                jASkin.getButton().setTranslateY(mouseEvent.getSceneY() - yStart);
                currentX = mouseEvent.getSceneX() - xStart;
                currentY = mouseEvent.getSceneY() - yStart;
                xOnMove.accept(currentX);
                yOnMove.accept(currentY);
            } else {
                System.out.println("Stop!");
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

            //check if min max value are ok
            if (value < xMinNormValue) xMinNormValue = value;
            if (value > xMaxNormValue) xMaxNormValue = value;
            //scale axis from 0 to 1
            if (value < NORMALIZED_CENTER_POSITION) {
                value = (value - xMinNormValue) / (NORMALIZED_CENTER_POSITION - xMinNormValue) / 2;
            } else if (value > NORMALIZED_CENTER_POSITION) {
                value = 1 + (xMaxNormValue - value) / (NORMALIZED_CENTER_POSITION - xMaxNormValue) / 2;
            }

        if (!normalized0to1) {
            value = rescaleValue(value);
        }

            setNormX(value);
            updatePos();
            task.accept(value);

        };
 //       if (value < xMinNormValue) value = xMinNormValue;
   //     if (value > xMaxNormValue) value = xMaxNormValue;
     //   task.accept(value);
    }

    @Override
    public void yOnMove(Consumer<Double> task) {
        yOnMove = value -> {

        //check if min max value are ok
        if (value < yMinNormValue) yMinNormValue = value;
        if (value > yMaxNormValue) yMaxNormValue = value;
        //scale axis from 0 to 1
        if (value < NORMALIZED_CENTER_POSITION) {
            value = (value - yMinNormValue) / (NORMALIZED_CENTER_POSITION - yMinNormValue) / 2;
        } else if (value > NORMALIZED_CENTER_POSITION) {
            value = 1 + (yMaxNormValue - value) / (NORMALIZED_CENTER_POSITION - yMaxNormValue) / 2;
        }

        if (!normalized0to1) {
            value = rescaleValue(value);
        }

        setNormY(value);
        updatePos();

        task.accept(value);
    };

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

    private double rescaleValue(double in) {
        return (in - NORMALIZED_CENTER_POSITION) * 2;
    }

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
