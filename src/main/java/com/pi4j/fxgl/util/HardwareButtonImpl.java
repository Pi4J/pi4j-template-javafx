package com.pi4j.fxgl.util;

import java.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;

/**
 * Default implementation of HardwareButton to conveniently access a
 * real physical button attached to GPIO
 *
 * @author Dieter Holz
 */
public class HardwareButtonImpl implements HardwareButton {

    /**
     * Pi4J digital input instance used by this component
     */
    private DigitalInput digitalInput;

    /**
     * GPIO pin the button is attached
     */
    private final int bcmPin;

    /**
     * button's name
     */
    private final String label;

    // needed for whileDown handling
    private final ScheduledExecutorService scheduler;
    private       ScheduledFuture<?>       scheduledFuture;

    public HardwareButtonImpl(int bcmPin, String label) {
        this.bcmPin    = bcmPin;
        this.label     = label;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /** {@inheritDoc}
     */
    @Override
    public void initialize() {
        this.digitalInput = Pi4JContext.createDigitalInput(bcmPin, label);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }

    /**
     * all the actions that will be triggered while button is pressed
     */
    private final List<Runnable> whileDownListener = new ArrayList<>();

    /** {@inheritDoc}
     */
    @Override
    public void addOnPressed(Runnable action) {
        digitalInput.addListener(event -> {
            if (isPressed()) {
                action.run();
            }
        });
    }

    /** {@inheritDoc}
     */
    @Override
    public void addOnReleased(Runnable action) {
        digitalInput.addListener(event -> {
            if (isDepressed()) {
                action.run();
            }
        });
    }

    /** {@inheritDoc}
     */
    @Override
    public void addWhileDown(Runnable action, Duration pulse, HardwareButton... alsoPressedButton) {
        if (whileDownListener.isEmpty()) {
            digitalInput.addListener(event -> {
                if (isPressed() && Arrays.stream(alsoPressedButton).noneMatch(HardwareButton::isDepressed)) {
                    startWhileDown(pulse);
                } else {
                    stopWhileDown();
                }
            });
        }
        whileDownListener.add(action);
    }

    /** {@inheritDoc}
     */
    @Override
    public boolean isPressed() {
        return digitalInput.state() == DigitalState.LOW;
    }

    /** {@inheritDoc}
     */
    @Override
    public boolean isDepressed() {
        return !isPressed();
    }

    /** {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return label;
    }

    private void startWhileDown(Duration pulse) {
        if (!whileDownListener.isEmpty()) {
            stopWhileDown();
            scheduledFuture = scheduler.scheduleAtFixedRate(
                    () -> whileDownListener.forEach(Runnable::run),
                    0,
                    pulse.toMillis(),
                    TimeUnit.MILLISECONDS);
        }
    }

    private void stopWhileDown() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    @Override
    public HardwareButton getButtonDelegate() {
        throw new IllegalCallerException("should never be called");
    }

}
