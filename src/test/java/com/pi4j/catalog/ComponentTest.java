package com.pi4j.catalog;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import com.pi4j.context.Context;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProviderImpl;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProviderImpl;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProviderImpl;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProviderImpl;
import com.pi4j.plugin.mock.provider.spi.MockSpiProviderImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class ComponentTest {
    protected Context pi4j;

    @BeforeEach
    public final void setUpPi4J() {
        pi4j = (BoardInfoHelper.runningOnRaspberryPi()) ?
                Pi4J.newAutoContext() :
                Pi4J.newContextBuilder()
                        .add(new MockDigitalInputProviderImpl())
                        .add(new MockDigitalOutputProviderImpl())
                        .add(new MockPwmProviderImpl())
                        .add(new MockSpiProviderImpl())
                        .add(new MockI2CProviderImpl())
                        .build();
    }

    @AfterEach
    public void tearDownPi4J() {
        pi4j.shutdown();
    }
}
