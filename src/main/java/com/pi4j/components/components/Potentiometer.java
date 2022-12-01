package com.pi4j.components.components;

import com.pi4j.components.components.helpers.ContinuousMeasuringException;
import com.pi4j.config.exception.ConfigException;

import java.util.function.Consumer;

public class Potentiometer extends Component {
    /**
     * ads1115 instance
     */
    private final Ads1115 ads1115;

    /**
     * AD channel connected to potentiometer (must be between 0 and 3)
     */
    private final int channel;
    /**
     * min value which potentiometer has reached
     */
    private double minValue;

    /**
     * max value which potentiometer has reached
     */
    private double maxValue;

    /**
     * fast continuous reading is active
     */
    private boolean fastContinuousReadingActive = false;

    /**
     * slow continuous reading is active
     */
    private boolean slowContinuousReadingActive = false;

    /**
     * Create a new potentiometer component with custom channel and custom maxVoltage
     *
     * @param ads1115    ads instance
     * @param channel     custom ad channel
     * @param maxVoltage custom maxVoltage
     */
    public Potentiometer(Ads1115 ads1115, int channel, double maxVoltage) {
        this.ads1115  = ads1115;
        this.minValue = ads1115.getPga().gain() * 0.1;
        this.maxValue = maxVoltage;
        this.channel  = channel;

        //check if channel is in range of ad converter
        if (channel < 0 || channel > 3) {
            throw new ConfigException("Channel number for ad converter not possible, choose channel between 0 to 3");
        }
        logDebug("Build component potentiometer");
    }

    /**
     * Create a new potentiometer component with default channel and maxVoltage for Raspberry pi
     *
     * @param ads1115 ads instance
     */
    public Potentiometer(Ads1115 ads1115) {
        this.ads1115 = ads1115;
        this.minValue = ads1115.getPga().gain() * 0.1;
        this.maxValue = 3.3;
        this.channel = 0;

        logDebug("Build component potentiometer");
    }

    /**
     * Returns actual voltage from potentiometer
     *
     * @return voltage from potentiometer
     */
    public double singleShotGetVoltage() {
        double result = switch (channel) {
            case 0  -> ads1115.singleShotAIn0();
            case 1  -> ads1115.singleShotAIn1();
            case 2  -> ads1115.singleShotAIn2();
            case 3  -> ads1115.singleShotAIn3();
            default -> 0.0;
        };
        updateMinMaxValue(result);
        return result;
    }

    /**
     * Returns normalized value from 0 to 1
     *
     * @return normalized value
     */
    public double singleShotGetNormalizedValue() {
        return singleShotGetVoltage() / maxValue;
    }

    /**
     * Sets or disables the handler for the onValueChange event.
     * This event gets triggered whenever the value changes.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    public void setConsumerFastRead(Consumer<Double> method) {
        ads1115.setConsumerFastRead((value) -> {
            updateMinMaxValue(value);
            value = value / maxValue;
            method.accept(value);
        });
    }

    /**
     * Sets or disables the handler for the onValueChange event.
     * This event gets triggered whenever the value changes.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    public void setConsumerSlowReadChan(Consumer<Double> method) {
        switch (channel) {
            case 0:
                ads1115.setConsumerSlowReadChannel0((value) -> {
                    updateMinMaxValue(value);
                    value = value / maxValue;
                    method.accept(value);
                });
                break;
            case 1:
                ads1115.setConsumerSlowReadChannel1((value) -> {
                    updateMinMaxValue(value);
                    value = value / maxValue;
                    method.accept(value);
                });
                break;
            case 2:
                ads1115.setConsumerSlowReadChannel2((value) -> {
                    updateMinMaxValue(value);
                    value = value / maxValue;
                    method.accept(value);
                });
                break;
            case 3:
                ads1115.setConsumerSlowReadChannel3((value) -> {
                    updateMinMaxValue(value);
                    value = value / maxValue;
                    method.accept(value);
                });
                break;
        }
    }

    /**
     * start slow continuous reading. In this mode, up to 4 devices can be connected to the analog to digital
     * converter. For each device a single read command is sent to the ad converter and waits for the response.
     * The maximum sampling frequency of the analog signals depends on how many devices are connected to the AD
     * converter at the same time.
     * The maximum allowed sampling frequency of the signal is 1/2 the sampling rate of the ad converter.
     * The reciprocal of this sampling rate finally results in the minimum response time to a signal request.
     * (the delay of the bus is not included).
     * <p>
     * This leads to the following table for the maximum allowed readFrequency by a sampling rate of 128 sps:
     * 1 channel in use -> readFrequency max 64Hz (min. response time = 16ms)
     * 2 channel in use -> readFrequency max 32Hz (min. response time = 32ms)
     * 3 channel in use -> readFrequency max 21Hz (min. response time = 48ms)
     * 4 channel in use -> readFrequency max 16Hz (min. response time = 63ms)
     *
     * @param threshold     threshold for trigger new value change event (+- voltage)
     * @param readFrequency read frequency to get new value from device, must be lower than 1/2
     *                      sampling rate of device
     */
    public void startSlowContinuousReading(double threshold, int readFrequency) {
        if (fastContinuousReadingActive) {
            throw new ContinuousMeasuringException("fast continuous reading currently active");
        } else {
            //set slow continuous reading active to lock fast continuous reading
            slowContinuousReadingActive = true;
            ads1115.startSlowContinuousReading(channel, threshold, readFrequency);
        }
    }

    /**
     * stops slow continuous reading
     */
    public void stopSlowContinuousReading() {
        slowContinuousReadingActive = false;
        ads1115.stopSlowReadContinuousReading(channel);
    }

    /**
     * Starts fast continuous reading. In this mode only on device can be connected to the ad converter.
     * The maximum allowed readFrequency ist equal to the sample rate of the ad converter
     *
     * @param threshold     threshold for trigger new value change event (+- voltage)
     * @param readFrequency read frequency to get new value from device, must be lower than the
     *                      sampling rate of the device
     */
    public void startFastContinuousReading(double threshold, int readFrequency) {
        if (slowContinuousReadingActive) {
            throw new ContinuousMeasuringException("slow continuous reading currently active");
        } else {
            //set fast continuous reading active to lock slow continuous reading
            fastContinuousReadingActive = true;

            //start continuous reading on ads1115
            ads1115.startFastContinuousReading(channel, threshold, readFrequency);
        }
    }

    /**
     * stops fast continuous reading
     */
    public void stopFastContinuousReading() {
        fastContinuousReadingActive = false;
        //stop continuous reading
        ads1115.stopFastContinuousReading();
    }

    /**
     * disables all handlers
     */
    public void deregisterAll() {
        switch (channel) {
            case 0:
                ads1115.setConsumerSlowReadChannel0(null);
                break;
            case 1:
                ads1115.setConsumerSlowReadChannel1(null);
                break;
            case 2:
                ads1115.setConsumerSlowReadChannel2(null);
                break;
            case 3:
                ads1115.setConsumerSlowReadChannel3(null);
                break;
        }
    }

    /**
     * returns the maximum value which the potentiometer has reached in voltage
     *
     * @return maximal value in voltage
     */
    public double getMaxValue(){return maxValue;}

    /**
     * Check if new value is bigger than current max value or lower than min value
     * In this case update min or max value
     *
     * @param result value to check against min Max value
     */
    private void updateMinMaxValue(double result) {
        if (result < minValue) {
            minValue = result;
        } else if (result > maxValue) {
            maxValue = result;
        }
    }
}
