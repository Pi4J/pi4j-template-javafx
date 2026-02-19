package com.pi4j.catalog.components.base;

import java.util.List;
import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.plugin.mock.provider.pwm.MockPwm;

/**
 * Represents a PWM (Pulse Width Modulation) actuator component, capable of utilizing
 * hardware PWM to drive connected devices like motors, servos, or to make LEDs dimmable. This class
 * is intended to be used with Raspberry Pi GPIO pins configured for PWM output.
 *
 * The PWM actuator uses specific pins designated for PWM functionality. The supported
 * pins are defined in the {@code AVAILABLE_PWM_PINS} constant.
 *
 * You can check the PWM status of your Raspberry Pi with
 * 'jbang https://github.com/pi4j/pi4j-os/blob/main/iochecks/IOChecker.java pwm'
 *
 * Key features:
 * - Enables hardware PWM on specified GPIO pins.
 * - Supports configuration of PWM frequency and duty cycle.
 * - Ensures only valid pins configured for PWM are used.
 * - Provides a mockable interface for testing purposes in isolation.
 *
 * Constructor:
 * - Requires a Pi4J {@code Context} for managing the hardware interaction.
 * - Validates the provided GPIO pin for PWM compatibility.
 * - Configures the PWM channel and settings such as frequency and initial state.
 * - Automatically initializes the PWM to an off state.
 *
 * Notes:
 * - The GPIO pins must be configured to support PWM output, typically by adding
 *   `dtoverlay=pwm-2chan` to the Raspberry Pi configuration file.
 * - The `PWM_CHIP` value defines the underlying hardware PWM chip being used.
 * - Custom logic can be added by extending this class for specific PWM-driven
 *   components, like servo motors or dimmable LEDs.
 *
 * Shutdown Behavior:
 * - The {@code shutdown()} method ensures the PWM output is turned off, preventing
 *   unintended behavior when the component is no longer in use.
 *
 * Testing:
 * - Provides a {@code mock()} method for returning a mock version of the PWM instance,
 *   facilitating easier testing without needing actual hardware.
 */
public class PwmActuator extends Component {
    // You have two channels if 'dtoverlay=pwm-2chan' is added to /boot/firmware/config.txt
    private static final List<PIN> AVAILABLE_PWM_PINS = List.of(PIN.PWM18, PIN.PWM19);

    private static final int PWM_CHIP = 0;

    protected final Pwm pwm;

    protected PwmActuator(Context pi4J, PIN address, int frequency) {
        Objects.requireNonNull(pi4J);
        Objects.requireNonNull(address);
        if(!AVAILABLE_PWM_PINS.contains(address)){
            throw new IllegalArgumentException("Pin " + address + " is not a PWM Pin");
        }

        PwmConfig config = Pwm.newConfigBuilder(pi4J)
                .id("BCM" + address)
                .name("PWM Actuator #" + address)
                .pwmType(PwmType.HARDWARE)
                .chip(PWM_CHIP)
                .channel(address == PIN.PWM18 ? 2 : 3)
                .initial(0)
                .frequency(frequency)
                .shutdown(0)
                .initial(0)
                .shutdown(0)
                .build();
        pwm = pi4J.create(config);
    }

    @Override
    public void shutdown() {
        pwm.off();
        super.shutdown();
    }

    // --------------- for testing --------------------

    public MockPwm mock() {
        return asMock(MockPwm.class, pwm);
    }
}
