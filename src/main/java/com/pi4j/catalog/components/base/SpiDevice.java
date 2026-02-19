package com.pi4j.catalog.components.base;

import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiConfig;
import com.pi4j.plugin.mock.provider.spi.MockSpi;

/**
 * Represents an abstract SPI (Serial Peripheral Interface) device component.
 * Provides convenient functionality for communicating with SPI devices,
 * including writing, reading, and performing full-duplex data transfers.
 *
 * An example for an SPI device is an RFID-Reader.
 *
 * You can check the status of your Raspberry Pi's SPI port with
 * 'jbang https://github.com/pi4j/pi4j-os/blob/main/iochecks/IOChecker.java spi'
 *
 */
public abstract class SpiDevice extends Component {
    /**
     * The PI4J SPI
     */
    private final Spi spi;

    protected SpiDevice(Context pi4j, SpiConfig config){
        Objects.requireNonNull(pi4j);
        Objects.requireNonNull(config);

        spi = pi4j.create(config);
        logDebug("SPI is open");
    }

    /**
     * Writes the given bytes to the SPI device, discarding any received data.
     *
     * @param data Data to write to the device.
     */
    protected void spiWrite(byte... data) {
        spi.write(data);
    }

    /**
     * Performs a full-duplex transfer, writing and reading bytes simultaneously.
     * The returned buffer is the data received from the device.
     *
     * @param data Data to transmit to the device.
     */
    protected byte[] spiTransfer(byte... data) {
        spi.transfer(data);
        return data;
    }

    /**
     * Reads a specified number of bytes from the SPI device.
     * This is done by transmitting the same number of dummy bytes (0x00).
     *
     * @param bytesToRead The number of bytes to read.
     * @return A new byte array containing the data read from the device.
     */
    protected byte[] spiRead(int bytesToRead) {
        if (bytesToRead <= 0) {
            return new byte[0];
        }
        // To read, we must send dummy bytes to generate clock pulses.
        byte[] buffer = new byte[bytesToRead];

        // Perform the in-place transfer. 'buffer' will be filled with the read data.
        spi.transfer(buffer);

        // Return the buffer which now contains the data from the device.
        return buffer;
    }

    protected void sendToSerialDevice(byte[] data) {
        spi.write(data);
    }

    @Override
    public void shutdown() {
        if (spi.isOpen()) {
            spi.close();
        }
        logDebug("SPI device shut down.");
        super.shutdown();
    }

    // --------------- for testing --------------------

    public MockSpi mock() {
        return asMock(MockSpi.class, spi);
    }
}
