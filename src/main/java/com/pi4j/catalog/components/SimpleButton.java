package com.pi4j.catalog.components;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

import com.pi4j.catalog.components.base.DigitalSensor;
import com.pi4j.catalog.components.base.PIN;

import static com.pi4j.io.gpio.digital.DigitalInput.DEFAULT_DEBOUNCE;

/**
 * Represents a simple digital button component, built on the DigitalSensor base class.
 * <p>
 * This class provides methods to handle various button states and behaviors such as
 * pressed, released, and while-pressed event handling.
 */
public class SimpleButton extends DigitalSensor {
    /**
     * Specifies if the button state is inverted, e.g., HIGH = depressed, LOW = pressed
     * This will also automatically switch the pull resistance to PULL_UP
     */
    private final boolean inverted;

    /**
     * Runnable Code when the button is depressed
     */
    private Runnable onUp;

    /**
     * Runnable Code when the button is pressed
     */
    private Runnable onDown;

    /**
     * Handler while the button is pressed
     */
    private Runnable whileDown;

    /**
     * Timer while the button is pressed
     */
    private Duration whilePressedDelay;

    /**
     * what needs to be done while the button is pressed (and whilePressed is != null)
     */
    private final Runnable whileDownWorker = () -> {
        while (isDown()) {
            delay(whilePressedDelay);
            if (isDown() && whileDown != null) {
                logDebug("whileDown triggered");
                whileDown.run();
            }
        }
        whileDownFuture = null;
    };

    private ExecutorService executor;

    private Future<?> whileDownFuture = null;

    public SimpleButton(Context pi4j, PIN address) {
        this(pi4j, address, false, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component
     *
     * @param pi4j Pi4J context
     */
    public SimpleButton(Context pi4j, PIN address, boolean inverted) {
        this(pi4j, address, inverted, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component with a custom BCM-Pin and debounce time.
     *
     * @param pi4j     Pi4J context
     * @param address  GPIO address of button
     * @param inverted Specify if the button state is inverted
     * @param debounce Debounce time in microseconds
     */
    public SimpleButton(Context pi4j, PIN address, boolean inverted, long debounce) {
        super(pi4j,
              DigitalInput.newConfigBuilder(pi4j)
                      .id("BCM" + address)
                      .name("Button #" + address)
                      .bcm(address.getPin())
                      .debounce(debounce)
                      .pull(inverted ? PullResistance.PULL_UP : PullResistance.PULL_DOWN)
                      .build());

        this.inverted = inverted;

        /*
         * Gets a DigitalStateChangeEvent directly from the Provider, as this
         * Class is a listener. This runs in a different Thread than main.
         * Calls the methods onUp, onDown and whilePressed. WhilePressed gets
         * executed in an own Thread, as to not block other resources.
         */
        digitalInput.addListener(stateChangeEvent -> {
            DigitalState state = getState();

            logDebug("Button switched to '%s'", state);

            switch (state) {
                case HIGH -> {
                    if (onDown != null) {
                        logDebug("onDown triggered");
                        onDown.run();
                    }
                    if (whileDown != null && whileDownFuture == null) {
                        whileDownFuture = executor.submit(whileDownWorker);
                    }
                }
                case LOW -> {
                    if (onUp != null) {
                        logDebug("onUp triggered");
                        onUp.run();
                    }
                }
                case UNKNOWN -> logError("Button is in State UNKNOWN");
            }
        });

        logDebug("Created new SimpleButton component on pin %s", address);
    }

    /**
     * Checks if the button is currently pressed.
     * <P>
     * For a not-inverted button this means: if the button is pressed, then full voltage is present
     * at the GPIO-Pin. Therefore, the DigitalState is HIGH
     *
     * @return true if the button is pressed
     */
    public boolean isDown() {
        return getState() == DigitalState.HIGH;
    }

    /**
     * Checks if the button is currently depressed (= NOT pressed)
     * <P>
     * For a not-inverted button this means: if the button is depressed, then no voltage is present
     * at the GPIO-Pin. Therefore, the DigitalState is LOW
     *
     * @return true if the button is depressed
     */
    public boolean isUp() {
        return getState() == DigitalState.LOW;
    }

    /**
     * Sets or disables the handler for the onDown event.
     * <P>
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onDown(Runnable task) {
        onDown = task;
    }

    /**
     * Sets or disables the handler for the onUp event.
     * <P>
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onUp(Runnable task) {
        onUp = task;
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * <P>
     * This event gets triggered as long as the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     * @param delay delay between two executions of the task
     */
    public void whilePressed(Runnable task, Duration delay) {
        Objects.requireNonNull(delay);

        whileDown = task;
        whilePressedDelay = delay;
        if(executor != null){
            executor.shutdownNow();
        }
        if(task != null){
            executor = Executors.newSingleThreadExecutor();
        }
    }

    public boolean isInInitialState(){
        return onDown == null && onUp == null && whileDown == null && executor == null;
    }

    /**
     * disables all the handlers for the onUp, onDown and WhileDown Events
     */
    @Override
    public void shutdown() {
        onDown          = null;
        onUp            = null;
        whileDown       = null;
        whileDownFuture = null;

        if(executor != null){
            executor.shutdown();
        }
        executor = null;
        super.shutdown();
    }

    /**
     * Returns the current state of the Digital State
     *
     * @return Current DigitalInput state (Can be HIGH, LOW or UNKNOWN)
     */
    private DigitalState getState() {
        return switch (digitalInput.state()) {
            case HIGH -> inverted ? DigitalState.LOW  : DigitalState.HIGH;
            case LOW  -> inverted ? DigitalState.HIGH : DigitalState.LOW;
            default   -> DigitalState.UNKNOWN;
        };
    }

}
