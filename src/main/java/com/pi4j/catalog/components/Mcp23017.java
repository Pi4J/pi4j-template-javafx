package com.pi4j.catalog.components;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.i2c.I2C;

import com.pi4j.catalog.components.base.I2CDevice;
import com.pi4j.catalog.components.base.PIN;

/**
 * Wiring of the expander itself:
 *
 * GPIO BCM 17 to IA of expander
 * GPIO BCM 27 to IB of expander
 *
 * VCC to 5V (pin 4)
 * GND to GND (pin 6)
 * SCL to SCL1 (pin 5)
 * SDA to SDA1 (pin 3)
 */
public class Mcp23017 extends I2CDevice {
    // Registers
    private static final int IODIRA = 0x00;
    private static final int IODIRB = 0x01;

    private static final int IPOLA = 0x02;
    private static final int IPOLB = 0x03;

    private static final int GPINTENA = 0x04;
    private static final int GPINTENB = 0x05;

    private static final int DEFVALA = 0x06;
    private static final int DEFVALB = 0x07;

    private static final int INTCONA = 0x08;
    private static final int INTCONB = 0x09;

    private static final int IOCON = 0x0A;

    private static final int GPPUA = 0x0C;
    private static final int GPPUB = 0x0D;

    private static final int INTFA = 0x0E;
    private static final int INTFB = 0x0F;

    private static final int INTCAPA = 0x10;
    private static final int INTCAPB = 0x11;

    private static final int GPIOA = 0x12;
    private static final int GPIOB = 0x13;

    private static final int OLATA = 0x14;
    private static final int OLATB = 0x15;

    private static final int DEFAULT_DEVICE_NUMBER = 0x27;
    private static final PIN DEFAULT_INTA_PIN = PIN.D17;
    private static final PIN DEFAULT_INTB_PIN = PIN.D27;

    private final Map<ExpanderPin, List<PinChangeListener>> listeners = new ConcurrentHashMap<>();

    public Mcp23017(Context pi4J){
        this(pi4J, DEFAULT_DEVICE_NUMBER, DEFAULT_INTA_PIN, DEFAULT_INTB_PIN);
    }

    public Mcp23017(Context pi4J,
                    int deviceNumber,
                    PIN intAGpio,
                    PIN intBGpio) {
        super(pi4J, deviceNumber, "MCP23017");


        initializeChip();

        if (intAGpio != null) {
            initializeInterruptA(pi4J, intAGpio);
        }

        if (intBGpio != null) {
            initializeInterruptB(pi4J, intBGpio);
        }
    }

    @Override
    protected void init(I2C i2c) {
        //nothing to do
    }

    @Override
    public void shutdown() {
        super.shutdown();
        writePortA(0x00);
        writePortB(0x00);
        clearInterrupts();
    }

    private void initializeChip() {
        /*
         IOCON:
         Bit 6 = MIRROR = 1
         Bit 2 = ODR = 1 (open drain interrupts)

         0b01000100 = 0x44
         */
        writeRegister(IOCON, 0x44);

        clearInterrupts();
    }

    private void clearInterrupts() {
        readRegister(INTCAPA);
        readRegister(INTCAPB);

        readRegister(GPIOA);
        readRegister(GPIOB);

        readRegister(INTFA);
        readRegister(INTFB);
    }

    private void initializeInterruptA(Context pi4J, PIN gpioPin) {
        DigitalInput intAInput = pi4J.create(
                DigitalInput.newConfigBuilder(pi4J)
                        .id("mcp23017-intA")
                        .name("MCP23017 INTA")
                        .bcm(gpioPin.getPin())
                        .pull(PullResistance.PULL_UP)
                        .debounce(5000L)
                        .build()
        );

        intAInput.addListener(event -> handleInterrupt(true));
    }

    private void initializeInterruptB(Context pi4J, PIN gpioPin) {
        DigitalInput intBInput = pi4J.create(
                DigitalInput.newConfigBuilder(pi4J)
                        .id("mcp23017-intB")
                        .name("MCP23017 INTB")
                        .bcm(gpioPin.getPin())
                        .pull(PullResistance.PULL_UP)
                        .debounce(5000L)
                        .build()
        );

        intBInput.addListener(event -> handleInterrupt(false));
    }

    public void configure(ExpanderPin expanderPin, PinOptions options) {
        // Direction
        setBit(register(IODIRA, IODIRB, expanderPin), expanderPin.mask(), options.mode == PinMode.INPUT);

        // Pull-up
        setBit(register(GPPUA, GPPUB, expanderPin), expanderPin.mask(), options.pullUp);

        // Polarity inversion
        setBit(register(IPOLA, IPOLB, expanderPin), expanderPin.mask(), options.inverted);

        switch (options.interruptMode) {
            case NONE -> setBit(register(GPINTENA, GPINTENB, expanderPin), expanderPin.mask(), false);

            case CHANGE -> {
                setBit(register(INTCONA, INTCONB, expanderPin), expanderPin.mask(), false);
                setBit(register(GPINTENA, GPINTENB, expanderPin), expanderPin.mask(), true);
            }

            case COMPARE_TO_DEFAULT -> {
                setBit(register(DEFVALA, DEFVALB, expanderPin), expanderPin.mask(), options.defaultValue);
                setBit(register(INTCONA, INTCONB, expanderPin), expanderPin.mask(), true);
                setBit(register(GPINTENA, GPINTENB, expanderPin), expanderPin.mask(), true);
            }
        }
    }

    public void addListener(ExpanderPin expanderPin, PinChangeListener listener) {
        listeners.computeIfAbsent(expanderPin, p -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void removeListener(ExpanderPin expanderPin, PinChangeListener listener) {
        List<PinChangeListener> pinListeners = listeners.get(expanderPin);
        if (pinListeners != null) {
            pinListeners.remove(listener);
        }
    }

    public void digitalWrite(ExpanderPin expanderPin, DigitalState state) {
        setBit(register(OLATA, OLATB, expanderPin), expanderPin.mask(), state == DigitalState.HIGH);
    }

    public DigitalState digitalRead(ExpanderPin expanderPin) {
        int value = readRegister(register(GPIOA, GPIOB, expanderPin));

        return (value & expanderPin.mask()) != 0
                ? DigitalState.HIGH
                : DigitalState.LOW;
    }

    public int readPortA() {
        return readRegister(GPIOA);
    }

    public int readPortB() {
        return readRegister(GPIOB);
    }

    public void writePortA(int value) {
        writeRegister(OLATA, value);
    }

    public void writePortB(int value) {
        writeRegister(OLATB, value);
    }

    private void handleInterrupt(boolean portA) {
        int intf     = readRegister(portA ? INTFA : INTFB);
        int captured = readRegister(portA ? INTCAPA : INTCAPB);

        for (ExpanderPin expanderPin : ExpanderPin.values()) {
            if (expanderPin.isPortA() != portA) {
                continue;
            }

            if ((intf & expanderPin.mask()) != 0) {
                DigitalState state = (captured & expanderPin.mask()) != 0
                                ? DigitalState.HIGH
                                : DigitalState.LOW;

                notifyListeners(expanderPin, state);
            }
        }
    }

    private void notifyListeners(ExpanderPin expanderPin, DigitalState state) {
        List<PinChangeListener> pinListeners = listeners.get(expanderPin);

        if (pinListeners == null) {
            return;
        }

        for (PinChangeListener listener : pinListeners) {
            listener.onChange(expanderPin, state);
        }
    }

    private int register(int portARegister, int portBRegister, ExpanderPin expanderPin) {
        return expanderPin.isPortA()
                ? portARegister
                : portBRegister;
    }

    private void setBit(int register, int mask, boolean enabled) {
        int value = readRegister(register);

        if (enabled) {
            value |= mask;
        } else {
            value &= ~mask;
        }

        writeRegister(register, value);
    }

    public enum PinMode {
        INPUT,
        OUTPUT
    }

    public enum InterruptMode {
        NONE,
        CHANGE,
        COMPARE_TO_DEFAULT
    }

    public enum ExpanderPin {
        A0(0), A1(1), A2(2), A3(3),
        A4(4), A5(5), A6(6), A7(7),

        B0(8), B1(9), B2(10), B3(11),
        B4(12), B5(13), B6(14), B7(15);

        private final int bit;

        ExpanderPin(int bit) {
            this.bit = bit;
        }

        public int bit() {
            return bit & 7;
        }

        public int mask() {
            return 1 << bit();
        }

        public boolean isPortA() {
            return bit < 8;
        }
    }

    public interface PinChangeListener {
        void onChange(ExpanderPin expanderPin, DigitalState state);
    }

    public static class PinOptions {
        public PinMode mode = PinMode.INPUT;
        public boolean pullUp = false;
        public boolean inverted = false;

        public InterruptMode interruptMode = InterruptMode.NONE;
        public boolean defaultValue = false;

        public static PinOptions input() {
            return new PinOptions();
        }

        public static PinOptions output() {
            PinOptions options = new PinOptions();
            options.mode = PinMode.OUTPUT;
            return options;
        }

        public PinOptions pullUp(boolean value) {
            this.pullUp = value;
            return this;
        }

        public PinOptions inverted(boolean value) {
            this.inverted = value;
            return this;
        }

        public PinOptions interruptOnChange() {
            this.interruptMode = InterruptMode.CHANGE;
            return this;
        }

        public PinOptions interruptCompareDefault(boolean defaultValue) {
            this.interruptMode = InterruptMode.COMPARE_TO_DEFAULT;
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
