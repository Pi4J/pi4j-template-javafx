package com.pi4j.mvc.util;

import java.util.List;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider;
import com.pi4j.plugin.pigpio.provider.serial.PiGpioSerialProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pi4JContext is made for applications with GUI and PUI.
 * <p>
 * These applications need to run on desktop (for development) and on Raspberry Pi.
 * <p>
 * In desktop environment the Pi4J MockPlatform is used, on Raspberry Pi the Pi4J RaspberryPiPlatform.
 */
public class Pi4JContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pi4JContext.class);

    private Pi4JContext() {
    }

    /**
     * Creates a new Pi4J Context depending on the machine the app is running.
     *
     * @return Context that will be used by this app
     */
    public static Context createContext() {
        Context context = runsOnPi() ? createRaspPiContext() : createMockContext();
        LOGGER.info("GPIO initialized for {}", (runsOnPi() ? " RaspPi" : " desktop"));

        return context;
    }

    /**
     * Tries to find out whether we are running on desktop or on Raspberry Pi.
     *
     * @return true if game is running on Raspberry Pi
     */
    private static boolean runsOnPi() {
        String vendor = System.getProperty("java.vendor");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        List<String> piArchs = List.of("aarch64", "arm");

        return "Raspbian".equals(vendor) ||
               ("Linux".equals(osName) && piArchs.contains(osArch));
    }

    /**
     * This is needed in 'Desktop Mode' when we don't have a real GPIO
     *
     * @return Context that will be used by this app
     */
    public static Context createMockContext() {
        return Pi4J.newContextBuilder()
                   .add(new MockPlatform())
                   .add(MockAnalogInputProvider.newInstance(),
                        MockAnalogOutputProvider.newInstance(),
                        MockSpiProvider.newInstance(),
                        MockPwmProvider.newInstance(),
                        MockSerialProvider.newInstance(),
                        MockI2CProvider.newInstance(),
                        MockDigitalInputProvider.newInstance(),
                        MockDigitalOutputProvider.newInstance())
                   .build();
    }

    /**
     * Create a new Pi4J Context using PiGpio
     *
     * @return Context that will be used by this app
     */
    private static Context createRaspPiContext() {
        // Initialize PiGPIO
        final PiGpio piGpio = PiGpio.newNativeInstance();

        // Build Pi4J context with this platform and PiGPIO providers
        return Pi4J.newContextBuilder()
                   .noAutoDetect()
                   .add(new RaspberryPiPlatform() {
                       @Override
                       protected String[] getProviders() {
                           return new String[]{};
                       }
                   })
                   .add(PiGpioDigitalInputProvider.newInstance(piGpio),
                        PiGpioDigitalOutputProvider.newInstance(piGpio),
                        PiGpioPwmProvider.newInstance(piGpio),
                        PiGpioSerialProvider.newInstance(piGpio),
                        PiGpioSpiProvider.newInstance(piGpio),
                        LinuxFsI2CProvider.newInstance()
                       )
                   .build();
    }

}
