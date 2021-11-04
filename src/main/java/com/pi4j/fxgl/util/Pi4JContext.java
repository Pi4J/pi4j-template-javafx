package com.pi4j.fxgl.util;

import java.time.Duration;

import java.util.List;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.library.pigpio.PiGpio;
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
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pi4JContext is made for FXGL games running on arcade consoles.
 *
 * FXGL games need to run on desktop (at least for development) and on Raspberry Pi.
 *
 * In desktop environment the Pi4J MockPlatform is used, on Raspberry Pi the Pi4J RaspberryPiPlatform.
 *
 * Typically arcade consoles just provide some DigitalInput.
 *
 * @author Dieter Holz
 */
public class Pi4JContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pi4JContext.class);

    private static final Context INSTANCE = buildNewContext();

    /**
     * Default debounce time in microseconds
     */
    private static final long DEBOUNCE = 10_000L;

    private Pi4JContext() {
    }

    /**
     * DigitalInputs are needed get access to arcade buttons from your Java app.
     *
     * @param bcmPin the pin in bcm numbering scheme
     * @param label the label of the button
     *
     * @return DigitalInput to access the arcade button at bcmPin
     */
    public static DigitalInput createDigitalInput(int bcmPin, String label) {
        return INSTANCE.create(buildDigitalInputConfig(bcmPin, label));
    }

    /**
     * Properly shuts down the Pi4J context.
     */
    public static void shutdown() {
        INSTANCE.shutdown();
        //give it some time (don't know if that's really necessary)
        delay(Duration.ofMillis(200));
    }

    /**
     * Creates a new Pi4J Context depending on the machine the app is running.
     *
     * @return Context that will be used by this app
     */
    private static Context buildNewContext() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
            LOGGER.info("GPIO shutdown");
        }));

        Context context = runsOnPi() ? createContext() : createMockContext();
        LOGGER.info("GPIO initialized for " + (runsOnPi() ? " RaspPi" : " desktop"));

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
    private static Context createMockContext() {
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
     * There may be situations where an 'old' Context wasn't properly shutdowned.
     *
     * On this old context we call 'shutdown' before creating a new context, that is used by the app.
     *
     * @return Context that will be used by this app
     */
    private static Context createCleanContext() {
        Context oldContext = createContext();
        oldContext.shutdown();

        delay(Duration.ofMillis(200));

        return createContext();
    }

    /**
     * Create a new Pi4J Context using PiGpio
     *
     * @return  Context that will be used by this app
     */
    private static Context createContext() {
        // Initialize PiGPIO
        final var piGpio = PiGpio.newNativeInstance();

        // Build Pi4J context with this platform and PiGPIO providers
        return Pi4J.newContextBuilder()
                   .noAutoDetect()
                   .add(new RaspberryPiPlatform() {
                       @Override
                       protected String[] getProviders() {
                           return new String[]{};
                       }
                   })
                   .add(PiGpioDigitalInputProvider.newInstance(piGpio))  // on our arcade console we just have digitalInput
                   .build();
    }

    /**
     *
     * @param bcm
     * @param label
     * @return
     */
    private static DigitalInputConfig buildDigitalInputConfig(int bcm, String label) {
        return DigitalInput.newConfigBuilder(INSTANCE)
                           .id("BCM_" + bcm)
                           .name(label)
                           .address(bcm)
                           .debounce(DEBOUNCE)
                           .pull(PullResistance.PULL_UP)
                           .build();
    }

    /**
     * Just let the current thread sleep for some time.
     *
     * @param duration
     */
    private static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
