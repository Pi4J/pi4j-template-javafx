package com.pi4j.catalog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;

public abstract class ComponentTest {
    protected Context pi4j;

    @BeforeEach
    public final void setUpPi4J() {
        pi4j = Pi4J.newContextBuilder()
                .add(new MockPlatform())
                .add(
                        MockAnalogInputProvider.newInstance(),
                        MockAnalogOutputProvider.newInstance(),
                        MockSpiProvider.newInstance(),
                        MockPwmProvider.newInstance(),
                        MockSerialProvider.newInstance(),
                        MockI2CProvider.newInstance(),
                        MockDigitalInputProvider.newInstance(),
                        MockDigitalOutputProvider.newInstance()
                )
                .build();
    }

    @AfterEach
    public void tearDownPi4J() {
        pi4j.shutdown();
    }
}
