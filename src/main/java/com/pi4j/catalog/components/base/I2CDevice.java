package com.pi4j.catalog.components.base;

import java.time.Duration;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.plugin.mock.provider.i2c.MockI2C;

public abstract class I2CDevice extends Component {

    /**
     * The Default BUS and Device Address.
     * On the PI, you can look it up with the Command 'sudo i2cdetect -y 1'
     */
    protected static final int DEFAULT_BUS = 0x01;

    /**
     * The PI4J I2C component
     */
    private final I2C i2c;


    protected I2CDevice(Context pi4j, int device, String name){
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
     * send a single command to device
     */
    protected void sendCommand(byte cmd) {
        i2c.write(cmd);
        delay(Duration.ofNanos(100_000));
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
     * send some data to device
     *
     * @param data
     */
    protected void write(byte data){
        i2c.write(data);
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
