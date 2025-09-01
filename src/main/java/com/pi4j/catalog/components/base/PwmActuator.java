package com.pi4j.catalog.components.base;

import java.util.List;
import java.util.Objects;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.plugin.mock.provider.pwm.MockPwm;

public class PwmActuator extends Component {
    // You have two channels if 'dtoverlay=pwm-2chan' is added to /boot/firmware/config.txt
    private static final List<PIN> AVAILABLE_PWM_PINS = List.of(PIN.PWM18, PIN.PWM19);

    protected final Pwm pwm;

    protected PwmActuator(Context pi4J, PwmConfig config) {
        Objects.requireNonNull(pi4J);
        Objects.requireNonNull(config);

        pwm = pi4J.create(config);
    }

    @Override
    public void shutdown() {
        pwm.off();
    }

    protected static int getAddress(PIN pin){
        if(!AVAILABLE_PWM_PINS.contains(pin)){
            throw new IllegalArgumentException("Pin " + pin + " is not a PWM Pin");
        }
        return runningOnPi5() ? (pin == PIN.PWM18 ? 2 : 3) : pin.getPin();
    }

    // --------------- for testing --------------------

    public MockPwm mock() {
        return asMock(MockPwm.class, pwm);
    }
}
