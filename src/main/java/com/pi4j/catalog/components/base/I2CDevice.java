package com.pi4j.catalog.components.base;

import java.time.Duration;
import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.plugin.mock.provider.i2c.MockI2C;

/**
 * This abstract class represents an I2C device and provides foundational functionality for
 * communicating with hardware components via the I2C protocol.
 *
 * You can check the PWM status of your Raspberry Pi with
 * 'jbang https://github.com/pi4j/pi4j-os/blob/main/iochecks/IOChecker.java i2c'
 *
 * It serves as the base class for specific device implementations.
 */
public abstract class I2CDevice extends Component {

    public static final Duration PAUSE_AFTER_WRITE = Duration.ofNanos(500_000);

    /**
     * The Default BUS and Device Address.
     * On the PI, you can look it up with the Command 'sudo i2cdetect -y 1'
     */
    protected static final int DEFAULT_BUS = 0x1;

    /**
     * The PI4J I2C component
     */
    private final I2C i2c;

    protected I2CDevice(Context pi4j, int device, String name){
        Objects.requireNonNull(pi4j);

        i2c = pi4j.create(I2C.newConfigBuilder(pi4j)
                  .id("I2C-" + DEFAULT_BUS + "@" + device)
                  .name(name+ "@" + device)
                  .bus(DEFAULT_BUS)
                  .device(device)
                  .build());
        init(i2c);
        logDebug("I2C device %s initialized", name);
    }


    /**
     * send a single command to the device
     */
    protected void sendCommand(byte cmd) {
        i2c.write((byte) 0x00, cmd);
        delay(PAUSE_AFTER_WRITE);
    }

    /**
     * send some data to the device
     *
     * @param data
     */
    protected void write(byte data){
        i2c.write((byte)0x40, data);
        delay(PAUSE_AFTER_WRITE);
    }

    protected int readRegister(int register) {
        return i2c.readRegisterWord(register);
    }

    /**
     * send custom configuration to device
     *
     * @param config custom configuration
     */
    protected void writeRegister(int register, int config) {
        i2c.writeRegisterWord(register, config);
    }

    /**
     * Execute Display commands
     *
     * @param command Select the LCD Command
     * @param data    Setup command data
     */
    protected void sendCommand(byte command, byte data) {
        sendCommand((byte) (command | data));
    }

    protected abstract void init(I2C i2c);

    // --------------- for testing --------------------

    public MockI2C mock() {
        return asMock(MockI2C.class, i2c);
    }

}
