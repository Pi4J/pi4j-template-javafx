package com.pi4j.components.components;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.context.Context;

import java.util.function.Consumer;

public class JoystickAnalog extends Component implements com.pi4j.components.interfaces.JoystickAnalogInterface {
    /**
     * potentiometer x axis
     */
    private final Potentiometer x;
    /**
     * potentiometer y axis
     */
    private final Potentiometer y;
    /**
     * button push
     */
    private final SimpleButton push;
    /**
     * default channel for potentiometer x-axis
     */
    private static final int DEFAULT_CHANNEL_POTENTIOMETER_X = 0;
    /**
     * default channel for potentiometer x-axis
     */
    private static final int DEFAULT_CHANNEL_POTENTIOMETER_Y = 1;
    /**
     * default max voltage for raspberry pi
     */
    private static final double DEFAULT_MAX_VOLTAGE = 3.3;
    /**
     * default normalization if true -> normalization from 0 to 1
     * if false -> normalization from -1 to 1
     */
    private static final boolean DEFAULT_NORMALIZATION = true;
    /**
     * normalized center position
     */
    private final double NORMALIZED_CENTER_POSITION = 0.5;
    /**
     * offset center x-axis
     */
    private double xOffset = 0.0;
    /**
     * offset center y-axis
     */
    private double yOffset = 0.0;
    /**
     * if true normalized axis from 0 to 1 center is 0.5, if false normalized axis from -1 to 1 center is 0
     */
    private final boolean normalized0to1;
    /**
     * minimal normalized value on x axis
     */
    private double xMinNormValue;
    /**
     * maximal normalized value on x axis
     */
    private double xMaxNormValue;
    /**
     * minimal normalized value on y axis
     */
    private double yMinNormValue;
    /**
     * maximal normalized value on y axis
     */
    private double yMaxNormValue;

    /**
     * Builds a new JoystickAnalog component with default configuration for raspberry pi with ads1115 object
     *
     * @param pi4j    Pi4J context
     * @param ads1115 ads object
     * @param push    additional push button on joystick
     */
    public JoystickAnalog(Context pi4j, Ads1115 ads1115, PIN push) {
        this(pi4j, ads1115, DEFAULT_CHANNEL_POTENTIOMETER_X, DEFAULT_CHANNEL_POTENTIOMETER_Y, DEFAULT_MAX_VOLTAGE, DEFAULT_NORMALIZATION, push);
    }

    /**
     * Builds a new JoystickAnalog component with custom input for x-, y-axis, custom pin for push button.
     * ads component needs to be created outside this clas, other channels may be used for other components.
     *
     * @param pi4j        Pi4J context
     * @param ads1115     ads object
     * @param chanelXAxis analog potentiometer x-axis
     * @param chanelYAxis analog potentiometer y-axis
     * @param maxVoltage  max voltage expects on analog input x- and y-axis
     * @param normalized0to1 normalization axis if true -> normalization from 0 to 1 if false -> normalization from -1 to 1
     * @param push        additional push button on joystick
     */
    public JoystickAnalog(Context pi4j, Ads1115 ads1115, int chanelXAxis, int chanelYAxis, double maxVoltage, boolean normalized0to1, PIN push) {
        this(new Potentiometer(ads1115, chanelXAxis, maxVoltage), new Potentiometer(ads1115, chanelYAxis, maxVoltage), normalized0to1, new SimpleButton(pi4j, push, true));
    }

    /**
     * @param potentiometerX potentiometer object for x-axis
     * @param potentiometerY potentiometer object for y-axis
     * @param normalized0to1 normalization axis if true -> normalization from 0 to 1 if false -> normalization from -1 to 1
     * @param push           simpleButton object for push button on joystick
     */
    public JoystickAnalog(Potentiometer potentiometerX, Potentiometer potentiometerY, boolean normalized0to1, SimpleButton push) {
        this.x = potentiometerX;
        this.y = potentiometerY;
        this.push = push;
        this.normalized0to1 = normalized0to1;

        xMinNormValue = 0.1;
        xMaxNormValue = 0.9;
        yMinNormValue = 0.1;
        yMaxNormValue = 0.9;
    }

    /**
     * Sets or disables the handler for a value change event.
     * This event gets triggered whenever the x-axis of the joystick is moving.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void xOnMove(Consumer<Double> task) {
        x.setConsumerSlowReadChan((value) -> {

            value = value + xOffset;
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
            task.accept(value);
        });
    }

    /**
     * Sets or disables the handler for a value change event.
     * This event gets triggered whenever the y-axis of the joystick is moving.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void yOnMove(Consumer<Double> task) {
        y.setConsumerSlowReadChan((value) -> {
            value = value + yOffset;

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

            task.accept(value);
        });
    }

    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void pushOnDown(Runnable task) {
        push.onDown(task);
    }

    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void pushOnUp(Runnable task) {
        push.onUp(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void pushWhilePressed(Runnable task, long whilePressedDelay) {
        push.whilePressed(task, whilePressedDelay);
    }

    /**
     * Start reading of joystick value. Needs to be triggered before any value can be read.
     *
     * @param threshold     delta between old and new value to trigger new event (+- voltage)
     * @param readFrequency update frequency to read new value from ad converter
     */
    public void start(double threshold, int readFrequency) {
        x.startSlowContinuousReading(threshold, readFrequency);
        y.startSlowContinuousReading(threshold, readFrequency);
    }

    /**
     * Stop reading of joystick value. If triggered no new value from joystick can be read.
     */
    public void stop() {
        x.stopSlowContinuousReading();
        y.stopSlowContinuousReading();
    }

    /**
     * disables all the handlers on joystick events
     */
    @Override
    public void deregisterAll() {
        x.deregisterAll();
        y.deregisterAll();
        push.deRegisterAll();
    }

    /**
     * calibrates the center position of the joystick
     */
    @Override
    public void calibrateJoystick() {
        xOffset = NORMALIZED_CENTER_POSITION - x.singleShotGetNormalizedValue();
        yOffset = NORMALIZED_CENTER_POSITION - y.singleShotGetNormalizedValue();
    }

    /**
     * returns xOffset
     * @return double xOffset
     */
    public double getX_Offset(){return xOffset;}

    /**
     * returns yOffset
     * @return double yOffset
     */
    public double getY_Offset(){return yOffset;}

    /**
     * Changes the output value from 0 to 1 to -1 to 1
     *
     * @param in original output value between 0 and 1
     * @return new output value between -1 and 1
     */
    private double rescaleValue(double in) {
        return (in - NORMALIZED_CENTER_POSITION) * 2;
    }
}
