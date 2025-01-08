package com.pi4j.catalog.components;

import java.util.function.Consumer;

import com.pi4j.catalog.components.base.Component;

/**
 * A potentiometer is an analog device and needs to use an analog-digital convertor (ADC) to be attached to RaspPi.
 * <p>
 * In this implementation we use an 'Ads115' and attach the potentiometer to one of the ADC channels.
 * <p>
 * We use the terms 'normalized value' and 'raw value'.
 * <ul>
 *     <li>Normalized values are between -1 and 1 ( 0 means that potentiometer is in center position) or between 0 and 1.
 *     Potentiometer can be configured which range it should use.
 *     </li>
 *     <li>Raw value is the measured voltage</li>
 * </ul>
 */
public class Potentiometer extends Component {
    public enum Range {
        ZERO_TO_ONE, MINUS_ONE_TO_ONE
    }

    /**
     * ads1115 instance
     */
    private final Ads1115 ads1115;
    private final Range range;

    /**
     * ADC channel the potentiometer is attached to
     */
    private final Ads1115.Channel channel;

    /**
     * Create a new potentiometer component attached to the specified ADC channel and preliminary values for raw value range
     *
     * @param ads1115 ADC instance
     */
    public Potentiometer(Ads1115 ads1115, Ads1115.Channel channel) {
        this(ads1115, channel, Range.ZERO_TO_ONE);
    }

    public Potentiometer(Ads1115 ads1115, Ads1115.Channel channel, Range range) {
        this.ads1115 = ads1115;
        this.range = range;
        this.channel = channel;

        logDebug("Potentiometer initialized");
    }

    /**
     * Returns actual voltage from potentiometer
     *
     * @return voltage from potentiometer
     */
    public double readCurrentVoltage() {
        return ads1115.readValue(channel);
    }

    /**
     * Returns normalized value from 0 to 1
     *
     * @return normalized value
     */
    public double readNormalizedValue() {
        return normalizeVoltage(readCurrentVoltage());
    }

    /**
     * Sets or disables the handler for the onValueChange event.
     * This event gets triggered whenever the value changes.
     * Only a single event handler can be registered at once.
     *
     * @param onChange Event handler to call or null to disable
     */
    public void onNormalizedValueChange(Consumer<Double> onChange) {
        ads1115.onValueChange(channel, (voltage) -> onChange.accept(normalizeVoltage(voltage)));
    }

    @Override
    public void reset() {
        ads1115.resetChannel(channel);
    }

    private double normalizeVoltage(double voltage) {
        double maxRawValue = ads1115.maxRawValue(channel);
        double minRawValue = ads1115.minRawValue(channel);

        if(range == Range.ZERO_TO_ONE){
            return Math.max(0.0, Math.min(voltage / (maxRawValue - minRawValue), 1.0));
        }
        else {
            double homeVoltage = (maxRawValue - minRawValue) * 0.5;
            return Math.max(-1.0, Math.min((voltage - homeVoltage) / homeVoltage,  1.0));
        }

    }
}
