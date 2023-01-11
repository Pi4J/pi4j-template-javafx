package com.pi4j.components.tiles;

import com.pi4j.components.components.Ads1115;
import com.pi4j.components.components.Potentiometer;
import com.pi4j.components.components.SimpleButton;
import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.JoystickAnalogInterface;
import com.pi4j.components.tiles.Skins.JoystickAnalogSkin;
import com.pi4j.context.Context;
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
    private long    whilePressedDelay;

    // Delay while button is pressed
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

    public JoystickAnalogTile(Context pi4J, Ads1115 ads1115, int channelXAxis, int channelYAxis, double maxVoltage, boolean normalized0to1, PIN push) {
        constructorValues();
        setText("Pin "+push.getPin()+", Channel: "+channelXAxis+" / "+ channelYAxis+ ", MaxVoltage: "+maxVoltage);
    }

    public JoystickAnalogTile(Context pi4j, Ads1115 ads1115, PIN push) {
        constructorValues();
        setText("Pin "+push.getPin());

    }
    public JoystickAnalogTile(Potentiometer potentiometerX, Potentiometer potentiometerY, boolean normalized0to1, SimpleButton push) {
        constructorValues();
    }


    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be catched and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This event gets triggered whenever the x-axis of the joystick is moving.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void xOnMove(Consumer<Double> task) {
        xOnMove = value -> {

            double xNormValue = jASkin.getBorder().getRadius();

            value = currentX;
            // Scale axis from 0 to 1
                value = 1 / xNormValue * value;

            setNormX(value);
            updatePos();
            task.accept(value);

        };
    }

    /**
     * This event gets triggered whenever the y-axis of the joystick is moving.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void yOnMove(Consumer<Double> task) {
        yOnMove = value -> {

        double yNormValue = jASkin.getBorder().getRadius();

            value = currentY;
            //scale axis from 0 to 1
            value = -1 / yNormValue * value;
            if (value == -0.0) value = 0.0;

        setNormY(value);
        updatePos();

        task.accept(value);
    };
    }

    /**
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void pushOnDown(Runnable task) {
        pushOnDown = task;
    }

    /**
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void pushOnUp(Runnable task) {
        pushOnUp = task;
    }

    /**
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
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
    public void start(double threshold, int readFrequency) {
    }

    @Override
    public void stop() {
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

    // Displays current X-Y-Axis on the GUI
    public void updatePos(){
        setDescription("("+String.format("%.2f", getNormX())+"/"+String.format("%.2f", getNormY())+")");
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(){
        setNormX(0.0);
        setNormY(0.0);
        setTitle("Joystick Analog");
        setSkin(jASkin);
        setDescription("("+String.format("%.2f", getNormX())+"/"+String.format("%.2f", getNormY())+")");

        jASkin.getButton().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - jASkin.getButton().getTranslateX();
            yStart = mouseEvent.getSceneY() - jASkin.getButton().getTranslateY();
        });

        // Moves Button inside the radius of the border
        // Sends X & Y Position to xOnMove & yOnMove
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

        // Resets Joystickbutton position
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
}
