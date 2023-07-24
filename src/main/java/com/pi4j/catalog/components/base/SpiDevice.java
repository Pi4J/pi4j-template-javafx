package com.pi4j.catalog.components.base;

import com.pi4j.context.Context;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiConfig;
import com.pi4j.plugin.mock.provider.spi.MockSpi;

public class SpiDevice extends Component {
    /**
     * The PI4J SPI
     */
    private final Spi spi;
    private final Context pi4j;

    protected SpiDevice(Context pi4j, SpiConfig config){
        this.pi4j = pi4j;
        spi = pi4j.create(config);
        logDebug("SPI is open");
    }

    protected void sendToSerialDevice(byte[] data) {
        spi.write(data);
    }

    @Override
    public void reset() {
        super.reset();
        spi.close();
        spi.shutdown(pi4j);
        logDebug("SPI closed");
    }

    // --------------- for testing --------------------

    public MockSpi mock() {
        return asMock(MockSpi.class, spi);
    }
}
