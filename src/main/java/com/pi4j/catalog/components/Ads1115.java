package com.pi4j.catalog.components;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;

import com.pi4j.catalog.components.base.I2CDevice;

public class Ads1115 extends I2CDevice {
    /**
     * The Conversion register contains the result of the last conversion.
     */
    private static final int CONVERSION_REGISTER = 0x00;
    /**
     * The Config register is used to change the ADS1115 operating modes and query the status of the device.
     */
    private static final int CONFIG_REGISTER = 0x01;
    /**
     * Lo_thresh set the lower threshold values used for the comparator function
     */
    private static final int LO_THRESH_REGISTER = 0x02;
    /**
     * Hi_thresh set the high threshold values used for the comparator function
     */
    private static final int HI_THRESH_REGISTER = 0x03;

    private final Context pi4j;
    /**
     * programmable gain amplifier
     */
    private final GAIN pga;
    /**
     * sampling rate of device
     */
    private final DataRate dataRate;

    /**
     * old values from last successful read of conversion register (raw data)
     */
    private final Map<Channel, Double> oldVoltages = new HashMap<>();

    private final Map<Channel, Consumer<Double>> channelsInUse = new HashMap<>();

    private final Map<Channel, RawValueRange> valueRanges = new HashMap<>();

    private boolean continuousReadingActive;

    /**
     * Config register default configuration
     */
    private final int configRegisterTemplate;


    public Ads1115(Context pi4j){
        this(pi4j, ADDRESS.GND, GAIN.GAIN_6_144V);
    }
    /**
     *
     * Creates a new custom AD converter
     *
     * @param pi4j    Pi4J context
     * @param gain    Custom gain amplifier
     */
    public Ads1115(Context pi4j, ADDRESS device, GAIN gain){
        super(pi4j, device.address, "ADS1115");
        this.pi4j = pi4j;

        this.pga = gain;
        this.dataRate = DataRate.SPS_128;

        int operationalStatus = OperationalStatus.WRITE_START.getOperationalStatus();
        int compMode          = COMP_MODE.TRAD_COMP.getCompMode();
        int compPol           = COMP_POL.ACTIVE_LOW.getCompPol();
        int latching          = COMP_LAT.NON_LATCH.getLatching();
        int compQue           = COMP_QUE.DISABLE_COMP.getCompQue();

        this.configRegisterTemplate = operationalStatus | pga.gain | dataRate.getConf() | compMode | compPol | latching | compQue;
    }

    Context getPi4j() {
        return pi4j;
    }

    @Override
    protected void init(I2C i2c) {
        //nothing to init
    }

    public void onValueChange(Channel channel, Consumer<Double> onChange) {
        if (onChange != null) {
            if (continuousReadingActive) {
                throw new IllegalStateException("Can't add a new onValueChange while continuous reading is active");
            }
            channelsInUse.put(channel, onChange);
        } else {
            channelsInUse.remove(channel);
        }
    }

    public double maxRawValue(Channel channel){
        return getRange(channel).maxRawValue;
    }

    public double minRawValue(Channel channel){
        return getRange(channel).minRawValue;
    }

    /**
     * Returns voltage value from specified channel
     *
     * @return double voltage
     */
    public double readValue(Channel channel) {
        if (continuousReadingActive) {
            throw new IllegalStateException("Continuous measuring active, can't read a single value");
        }

        double voltage = readSingleValue(channel);

        logDebug("current value of channel %s: %.2f", channel, voltage);

        return voltage;
    }


    /**
     * Start continuous reading. In this mode, up to 4 devices can be connected to the analog to digital
     * converter. For each device a single read command is sent to the ADC and waits for the response.
     * The maximum sampling frequency of the analog signals depends on how many devices are connected to the ADC.
     * <p>
     * The maximum allowed sampling frequency of the signal is 1/2 the sampling rate of the ADC.
     * The reciprocal of this sampling rate finally results in the minimum response time to a signal request.
     * (the delay of the bus is not included).
     * <p>
     * This leads to the following table for the maximum allowed readFrequency by a sampling rate of 128 sps:
     * 1 channels in use -> readFrequency max 64Hz (min. response time = 16ms)
     * 2 channels in use -> readFrequency max 32Hz (min. response time = 32ms)
     * 3 channels in use -> readFrequency max 21Hz (min. response time = 48ms)
     * 4 channels in use -> readFrequency max 16Hz (min. response time = 63ms)
     *
     * @param threshold  threshold for triggering value change event (+- voltage)
     */
    public void startContinuousReading(double threshold) {
        if (continuousReadingActive) {
            throw new IllegalStateException("continuous reading already active");
        } else {
            //set fast continuous reading active to lock slow continuous reading
            continuousReadingActive = true;

            readAllChannels(threshold);

            logDebug("Start continuous reading");
        }
    }

    /**
     * stops continuous reading
     */
    public void stopContinuousReading() {
         continuousReadingActive = false;

        logDebug("Continuous reading stopped");
    }

    /**
     * disables all handlers
     */
    @Override
    public void reset() {
        stopContinuousReading();
        delay(Duration.ofMillis(channelsInUse.size() * 16L));
        channelsInUse.clear();
        oldVoltages.clear();
    }

    public void resetChannel(Channel channel){
        channelsInUse.remove(channel);
        oldVoltages.remove(channel);
    }

    /**
     * Sends a request to device and wait for response
     *
     * @return value from conversion register
     */
    private double readSingleValue(Channel channel) {
        MultiplexerConfig mpc = switch (channel) {
            case A0 -> MultiplexerConfig.AIN0_GND;
            case A1 -> MultiplexerConfig.AIN1_GND;
            case A2 -> MultiplexerConfig.AIN2_GND;
            case A3 -> MultiplexerConfig.AIN3_GND;
        };

        //which channel should be available in ConfigRegister
        writeRegister(CONFIG_REGISTER, configRegisterTemplate | mpc.getMux() | OperationMode.SINGLE.getMode());
        //wait until ad converter has stored new value in conversion register
        //delay time is reciprocal of 1/2 of sampling time (*1000 from s to ms)
        delay(Duration.ofMillis((long) (2000.0 / dataRate.getSpS())));

        //now we can read the channel value from conversion register
        int registeredValue = readRegister(CONVERSION_REGISTER);

        double voltage = pga.gainPerBit * registeredValue;

        RawValueRange range = getRange(channel);
        range.maxRawValue = Math.max(range.maxRawValue, voltage);
        range.minRawValue = Math.min(range.minRawValue, voltage);

        return voltage;
    }

    /**
     * Sends, for each channel, a request to device and wait for response. Enters all responses in actualValue array.
     * Waits for the rest of readFrequency time.
     *
     * @param threshold threshold for trigger new value change event in Volt
     */
    private void readAllChannels(double threshold) {
        logDebug("Start continuous reading");
        final long readFrequency = channelsInUse.size() * 16 * 1_000_000L;

        //start new thread for continuous reading
        new Thread(() -> {
            while (continuousReadingActive) {
                //start measuring time
                long startTime = System.nanoTime();

                channelsInUse.forEach((channel, onValueChange) -> {
                    if(continuousReadingActive){ //can be set to false in the meantime
                        double newVoltage = readSingleValue(channel);
                        logDebug("Current value of channel %s: %d", channel, newVoltage);

                        double oldVoltage = getOldVoltage(channel);

                        if (Math.abs(oldVoltage - newVoltage) >= threshold) {
                            logDebug("New value change triggered on channel %s, old value: %f , new value: %f", channel, oldVoltage, newVoltage);
                            oldVoltages.put(channel, newVoltage);

                            onValueChange.accept(newVoltage);
                        }
                    }
                });

                long elapsedNanos = System.nanoTime() - startTime;
                long restDelay = elapsedNanos - readFrequency;

                //wait for rest of the cycle time
                delay(Duration.ofMillis(((restDelay > 0) ? restDelay : 0) / 1_000_000L));
            }
        }).start();
    }

    private double getOldVoltage(Channel channel){
        return oldVoltages.computeIfAbsent(channel, (c) -> -10.0);
    }

    private RawValueRange getRange(Channel channel){
        return valueRanges.computeIfAbsent(channel, (c) -> new RawValueRange());
    }

    private static class RawValueRange{
        double minRawValue = 0.1;
        double maxRawValue = 3.2;
    }

    public enum Channel {
        A0, A1, A2, A3
    }

    /**
     * I2C Address Selection
     * The ADS1115 has one address pin, ADDR, that configures the I2C address of the device. This pin can be
     * connected to GND, VDD, SDA, or SCL, allowing for four different addresses to be selected with one pin.
     * Use the GND, VDD and SCL addresses first. If SDA is used as the device address, hold the SDA line low
     * for at least 100 ns after the SCL line goes low to make sure the device decodes the address correctly
     * during I2C communication.
     * <p>
     * Address that can be used
     * {@link #GND}
     * {@link #VDD}
     * {@link #SDA}
     * {@link #SCL}
     */
    public enum ADDRESS {
        /**
         * Device address if pin is connected to GND
         */
        GND(0x48),
        /**
         * Device address if pin is connected to VDD
         */
        VDD(0x49),
        /**
         * Device address if pin is connected to SDA
         */
        SDA(0x4A),
        /**
         * Device address if pin is connected to SCL
         */
        SCL(0x4B);
        /**
         * device address on I2C
         */
        private final int address;

        /**
         * Set the address for a device on an I2C bus
         *
         * @param address device address on I2C
         */
        ADDRESS(int address) {
            this.address = address;
        }

        /**
         * Returns the address from the device on an I2C bus
         *
         * @return Returns the address form the device
         */
        public int getAddress() {
            return address;
        }
    }

    /**
     * A programmable gain amplifier (PGA) is implemented before the ΔΣ ADC of the ADS1115. The
     * full-scale range is configured by bits PGA[2:0] in the Config register and can be set to ±6.144 V, ±4.096 V,
     * ±2.048 V, ±1.024 V, ±0.512 V, ±0.256 V.
     * <p>
     * programmable gain amplifier that can be used
     * {@link #GAIN_6_144V}
     * {@link #GAIN_4_096V}
     * {@link #GAIN_2_048V}
     * {@link #GAIN_1_024V}
     * {@link #GAIN_0_512V}
     * {@link #GAIN_0_256V}
     */
    public enum GAIN {
        /**
         * 000 : Full-Scale Range (FSR)  = ±6.144 V
         */
        GAIN_6_144V(PGA.FSR_6_144.pga, 187.5 / 1_000_000.0),
        /**
         * 001 : FSR = ±4.096 V
         */
        GAIN_4_096V(PGA.FSR_4_096.pga, 125.0 / 1_000_000.0),
        /**
         * 010 : FSR = ±2.048 V
         */
        GAIN_2_048V(PGA.FSR_2_048.pga, 62.5 / 1_000_000.0),
        /**
         * 011 : FSR = ±1.024 V
         */
        GAIN_1_024V(PGA.FSR_1_024.pga, 31.25 / 1_000_000.0),
        /**
         * 100 : FSR = ±0.512 V
         */
        GAIN_0_512V(PGA.FSR_0_512.pga, 15.625 / 1_000_000.0),
        /**
         * 101 : FSR = ±0.256 V
         */
        GAIN_0_256V(PGA.FSR_0_256.pga, 7.8125 / 1_000_000.0);
        /**
         * bit structure for configuration
         */
        private final int gain;
        /**
         * gain per bit
         */
        private final double gainPerBit;

        /**
         * Set bit-structure for configuration and resolution (gain per bit)
         *
         * @param gain       configuration for gain
         * @param gainPerBit resolution
         */
        GAIN(int gain, double gainPerBit) {
            this.gain = gain;
            this.gainPerBit = gainPerBit;
        }

        /**
         * Returns bit structure for gain configuration
         *
         * @return bit structure for configuration
         */
        public int gain() {
            return gain;
        }

        /**
         * Return resolution gain per bit
         *
         * @return gain per bit
         */
        public double gainPerBit() {
            return gainPerBit;
        }
    }

    /**
     * Comparator queue and disable
     * These bits perform two functions. When set to 11, the comparator is disabled and
     * the ALERT/RDY pin is set to a high-impedance state. When set to any other
     * value, the ALERT/RDY pin and the comparator function are enabled, and the set
     * value determines the number of successive conversions exceeding the upper or
     * lower threshold required before asserting the ALERT/RDY pin.
     * <p>
     * teh following comparator queue can be used
     * {@link #ASSERT_ONE}
     * {@link #ASSERT_TWO}
     * {@link #ASSERT_FOUR}
     * {@link #DISABLE_COMP}
     */
    public enum COMP_QUE {
        /**
         * Assert after one conversion
         */
        ASSERT_ONE(0b0000_0000_0000_0000),
        /**
         * Assert after two conversions
         */
        ASSERT_TWO(0b0000_0000_0000_0001),
        /**
         * Assert after four conversions
         */
        ASSERT_FOUR(0b0000_0000_0000_0010),
        /**
         * Disable comparator and set ALERT/RDY pin to high-impedance
         */
        DISABLE_COMP(0b0000_0000_0000_0011),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0000_0000_0011),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_1111_1111_1100);
        /**
         * comparator queue
         */
        private final int compQue;

        /**
         * Set comparator queue for configuration
         *
         * @param compQue comparator queue configuration
         */
        COMP_QUE(int compQue) {
            this.compQue = compQue;
        }

        /**
         * Returns comparator queue configuration
         *
         * @return comparator queue configuration
         */
        public int getCompQue() {
            return this.compQue;
        }

    }

    /**
     * Latching comparator
     * This bit controls whether the ALERT/RDY pin latches after being asserted or
     * clears after conversions are within the margin of the upper and lower threshold
     * values.
     * <p>
     * the following mode can be used
     * {@link #NON_LATCH}
     * {@link #DO_LATCH}
     */
    public enum COMP_LAT {
        /**
         * Non latching comparator. The ALERT/RDY pin does not latch when asserted.
         */
        NON_LATCH(0b0000_0000_0000_0000),
        /**
         * Latching comparator. The asserted ALERT/RDY pin remains latched until
         * conversion data are read by the master or an appropriate SMBus alert response
         * is sent by the master. The device responds with its address, and it is the lowest
         * address currently asserting the ALERT/RDY bus line.
         */
        DO_LATCH(0b0000_0000_0000_0100),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0000_0000_0100),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_1111_1111_1011);
        private final int latching;

        /**
         * Set latching for configuration
         *
         * @param compLat comparator latching configuration
         */
        COMP_LAT(int compLat) {
            this.latching = compLat;
        }

        /**
         * Returns configuration for comparator latching
         *
         * @return comparator latching configuration
         */
        public int getLatching() {
            return this.latching;
        }
    }

    /**
     * Comparator polarity
     * This bit controls the polarity of the ALERT/RDY pin.
     * <p>
     * The following polarities can be set
     * {@link #ACTIVE_LOW}
     * {@link #ACTIVE_HIGH}
     */
    public enum COMP_POL {
        /**
         * Active low
         */
        ACTIVE_LOW(0b0000_0000_0000_0000),
        /**
         * Active high
         */
        ACTIVE_HIGH(0b0000_0000_0000_1000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0000_0000_1000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_1111_1111_0111);
        /**
         * comparator polarity
         */
        private final int compPol;

        /**
         * Set comparator polarisation for configuration
         *
         * @param compPol comparator polarisation for configuration
         */
        COMP_POL(int compPol) {
            this.compPol = compPol;
        }

        /**
         * Returns configuration of comparator polarisation
         *
         * @return comparator polarisation
         */
        public int getCompPol() {
            return this.compPol;
        }
    }

    /**
     * Comparator mode
     * This bit configures the comparator operating mode.
     * <p>
     * The following modes can be set
     * {@link #TRAD_COMP}
     * {@link #WINDOW_COMP}
     */
    public enum COMP_MODE {
        /**
         * Traditional comparator (
         */
        TRAD_COMP(0b0000_0000_0000_0000),
        /**
         * Window comparator
         */
        WINDOW_COMP(0b0000_0000_0001_0000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0000_0001_0000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_1111_1110_1111);
        /**
         * comparator mode
         */
        private final int compMode;

        /**
         * Set mode for comparator configuration
         *
         * @param compMOde comparator mode for configuration
         */
        COMP_MODE(int compMOde) {
            this.compMode = compMOde;
        }

        /**
         * Returns configuration of comparator mode
         *
         * @return configuration of comparator mode
         */
        public int getCompMode() {
            return this.compMode;
        }

    }

    /**
     * Data rate
     * These bits control the data rate setting
     * <p>
     * The following modes can be set
     * {@link #SPS_8}
     * {@link #SPS_16}
     * {@link #SPS_32}
     * {@link #SPS_64}
     * {@link #SPS_128}
     * {@link #SPS_250}
     * {@link #SPS_475}
     * {@link #SPS_860}
     */
    public enum DataRate {
        /**
         * 8 sampling per second
         */
        SPS_8(0b0000_0000_0000_0000, 8),
        /**
         * 16 sampling per second
         */
        SPS_16(0b0000_0000_0010_0000, 16),
        /**
         * 32 sampling per second
         */
        SPS_32(0b0000_0000_0100_0000, 32),
        /**
         * 64 sampling per second
         */
        SPS_64(0b0000_0000_0110_0000, 64),
        /**
         * 128 sampling per second
         */
        SPS_128(0b0000_0000_1000_0000, 128),
        /**
         * 250 sampling per second
         */
        SPS_250(0b0000_0000_1010_0000, 250),
        /**
         * 8475sampling per second
         */
        SPS_475(0b0000_0000_1100_0000, 475),
        /**
         * 860 sampling per second
         */

        SPS_860(0b0000_0000_1110_0000, 860),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0000_1110_0000, 0),

        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */

        CLR_CURRENT_CONF_PARAM(0b1111_1111_0001_1111, 0);
        /**
         * configuration
         */
        private final int conf;

        /**
         * sampling per second
         */
        private final int sps;

        /**
         * Set sampling rate for configuration
         *
         * @param sps sampling rate for configuration
         */
        DataRate(int conf, int sps) {
            this.conf = conf;
            this.sps = sps;
        }

        /**
         * Returns sampling rate
         *
         * @return sampling rate
         */
        public int getSpS() {
            return this.sps;
        }

        /**
         * Returns samplingrate for configuration
         *
         * @return sampling rate for configuration
         */
        public int getConf() {
            return this.conf;
        }
    }

    /**
     * Device operating mode
     * This bit controls the operating mode.
     * <p>
     * The following modes can be set
     * {@link #CONTINUOUS}
     * {@link #SINGLE}
     */
    public enum OperationMode {
        /**
         * Continuous-conversion mode
         */
        CONTINUOUS(0b0000_0000_0000_0000),
        /**
         * Single-shot mode or power-down state
         */
        SINGLE(0b0000_0001_0000_0000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_0001_0000_0000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_1110_1111_1111);
        /**
         * device operation mode
         */
        private final int mode;

        /**
         * Set device operation mode for configuration
         *
         * @param mode operation mode for configuration
         */
        OperationMode(int mode) {
            this.mode = mode;
        }

        /**
         * Returns configured operation mode
         *
         * @return configured operation mode
         */
        public int getMode() {
            return mode;
        }
    }

    /**
     * Programmable gain amplifier configuration
     * These bits set the FSR of the programmable gain amplifier.
     * <p>
     * The following gain amplifier can be configured
     * {@link #FSR_6_144}
     * {@link #FSR_4_096}
     * {@link #FSR_2_048}
     * {@link #FSR_1_024}
     * {@link #FSR_0_512}
     * {@link #FSR_0_256}
     */
    public enum PGA {
        /**
         * 000 : FSR = ±6.144 V
         */
        FSR_6_144(0b0000_0000_0000_0000),
        /**
         * 001 : FSR = ±4.096 V
         */
        FSR_4_096(0b0000_0010_0000_0000),
        /**
         * 010 : FSR = ±2.048 V
         */
        FSR_2_048(0b0000_0100_0000_0000),
        /**
         * 011 : FSR = ±1.024 V
         */
        FSR_1_024(0b0000_0110_0000_0000),
        /**
         * 100 : FSR = ±0.512 V
         */
        FSR_0_512(0b0000_1000_0000_0000),
        /**
         * 101 : FSR = ±0.256 V
         */
        FSR_0_256(0b0000_1010_0000_0000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0000_1110_0000_0000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1111_0001_1111_1111);
        /**
         * Programmable gain amplifier
         */
        private final int pga;

        /**
         * Set configuration for programmable gain amplifier
         *
         * @param pag Programmable gain amplifier configuration
         */
        PGA(int pag) {
            this.pga = pag;
        }

        /**
         * Returns configuration from Programmable gain amplifier
         *
         * @return Programmable gain amplifier configuration
         */
        public int getPga() {
            return this.pga;
        }

    }

    /**
     * Input multiplexer configuration
     * These bits configure the input multiplexer.
     * <p>
     * The following gain amplifier can be configured
     * {@link #AIN0_AIN1}
     * {@link #AIN0_AIN3}
     * {@link #AIN1_AIN3}
     * {@link #AIN2_AIN3}
     * {@link #AIN0_GND}
     * {@link #AIN1_GND}
     * {@link #AIN2_GND}
     * {@link #AIN3_GND}
     */
    public enum MultiplexerConfig {
        /**
         * 000 : AINP = AIN0 and AINN = AIN1
         */
        AIN0_AIN1(0b0000_0000_0000_0000),
        /**
         * 001 : AINP = AIN0 and AINN = AIN3
         */
        AIN0_AIN3(0b0001_0000_0000_0000),
        /**
         * 010 : AINP = AIN1 and AINN = AIN3
         */
        AIN1_AIN3(0b0010_0000_0000_0000),
        /**
         * 011 : AINP = AIN2 and AINN = AIN3
         */
        AIN2_AIN3(0b0011_0000_0000_0000),
        /**
         * 100 : AINP = AIN0 and AINN = GND
         */
        AIN0_GND(0b0100_0000_0000_0000),
        /**
         * 101 : AINP = AIN1 and AINN = GND
         */
        AIN1_GND(0b0101_0000_0000_0000),
        /**
         * 110 : AINP = AIN2 and AINN = GND
         */
        AIN2_GND(0b0110_0000_0000_0000),
        /**
         * 111 : AINP = AIN3 and AINN = GND
         */
        AIN3_GND(0b0111_0000_0000_0000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b0111_0000_0000_0000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b1000_1111_1111_1111);
        /**
         * Input multiplexer configuration
         */
        private final int mux;

        /**
         * Set configuration for Input multiplexer
         *
         * @param mux Input multiplexer configuration
         */
        MultiplexerConfig(int mux) {
            this.mux = mux;
        }

        /**
         * Returns configuration for Input multiplexer
         *
         * @return Input multiplexer configuration
         */
        public int getMux() {
            return mux;
        }
    }

    /**
     * Operational status or single-shot conversion start
     * This bit determines the operational status of the device. OS can only be written
     * when in power-down state and has no effect when a conversion is ongoing.
     * <p>
     * The following operation can be set
     * {@link #WRITE_START}
     * {@link #READ_CONV}
     * {@link #READ_NO_CONV}
     */
    public enum OperationalStatus {
        /**
         * When writing: start a single conversion (when in power-down state)
         */
        WRITE_START(0b1000_0000_0000_0000),
        /**
         * When reading: Device is currently performing a conversion
         */
        READ_CONV(0b0000_0000_0000_0000),
        /**
         * When reading: Device is not currently performing a conversion
         */
        READ_NO_CONV(0b1000_0000_0000_0000),
        /**
         * With an AND operation all other parameters will be set to 0
         */
        CLR_OTHER_CONF_PARAM(0b1000_0000_0000_0000),
        /**
         * With an AND operation the current parameters will be set to 0
         * all other parameters remain unchanged
         */
        CLR_CURRENT_CONF_PARAM(0b0111_1111_1111_1111);
        /**
         * Operational status or single-shot conversion start
         */
        private final int os;

        /**
         * Set parameter for os
         *
         * @param os parameter
         */
        OperationalStatus(int os) {
            this.os = os;
        }

        /**
         * Returns parameter for OS configuration
         *
         * @return os configuration parameter
         */
        public int getOperationalStatus() {
            return os;
        }
    }
}