package com.pi4j.catalog;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.catalog.components.Component;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.Spi;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2C;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwm;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpi;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public abstract class ComponentTest extends Component {
    protected Context pi4j;

    @SuppressWarnings("RedundantThrows")
    @BeforeEach
    public final void setUpBase() throws Exception {
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

    @SuppressWarnings("RedundantThrows")
    @AfterEach
    public void tearDownBase() throws Exception {
        pi4j.shutdown();
    }

    protected MockDigitalInput toMock(DigitalInput digitalInput) {
        return toMock(MockDigitalInput.class, digitalInput);
    }

    protected MockDigitalOutput toMock(DigitalOutput digitalOutput) {
        return toMock(MockDigitalOutput.class, digitalOutput);
    }

    protected MockDigitalOutput[] toMock(DigitalOutput[] digitalOutputs) {
        return Arrays.stream(digitalOutputs).map(this::toMock).toArray(MockDigitalOutput[]::new);
    }

    protected MockPwm toMock(Pwm pwm) {
        return toMock(MockPwm.class, pwm);
    }

    protected MockI2C toMock(I2C i2c) {
        return toMock(MockI2C.class, i2c);
    }

    protected MockSpi toMock(Spi spi) {
        return toMock(MockSpi.class, spi);
    }

    private <T> T toMock(Class<T> type, Object instance) {
        return type.cast(instance);
    }
}
