package com.pi4j.catalog.components;

import com.pi4j.context.Context;
import com.pi4j.catalog.components.helpers.PIN;
import com.pi4j.io.gpio.digital.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleButton extends Component {
    /**
     * Default debounce time in microseconds
     */
    private static final long DEFAULT_DEBOUNCE = 10_000;

    /**
     * Pi4J digital input instance used by this component
     */
    private final DigitalInput digitalInput;
    /**
     * Specifies if button state is inverted, e.g. HIGH = depressed, LOW = pressed
     * This will also automatically switch the pull resistance to PULL_UP
     */
    private final boolean inverted;
    /**
     * Runnable Code when button is pressed
     */
    private Runnable onDown;
    /**
     * Handler while button is pressed
     */
    private Runnable whilePressed;
    /**
     * Timer while button is pressed
     */
    private long whilePressedDelay;
    /**
     * Runnable Code when button is depressed
     */
    private Runnable onUp;
    /**
     * what needs to be done while button is pressed (and whilePressed is != null)
     */
    private final Runnable whilePressedWorker = () -> {
        while (isDown()) {
            delay(whilePressedDelay);
            if (isDown()) {
                whilePressed.run();
            }
        }
    };
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Creates a new button component
     *
     * @param pi4j Pi4J context
     */
    public SimpleButton(Context pi4j, PIN address, boolean inverted) {
        this(pi4j, address, inverted, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component with custom GPIO address and debounce time.
     *
     * @param pi4j     Pi4J context
     * @param address  GPIO address of button
     * @param inverted Specify if button state is inverted
     * @param debounce Debounce time in microseconds
     */
    public SimpleButton(Context pi4j, PIN address, boolean inverted, long debounce) {
        this.inverted = inverted;

        this.digitalInput = pi4j.create(buildDigitalInputConfig(pi4j, address, inverted, debounce));

        /*
         * Gets a DigitalStateChangeEvent directly from the Provider, as this
         * Class is a listener. This runs in a different Thread than main.
         * Calls the mehtods onUp, onDown and whilePressed. WhilePressed gets
         * executed in an own Thread, as to not block other resources.
         */
        this.digitalInput.addListener(digitalStateChangeEvent -> {
            DigitalState state = getState();

            logDebug("Button switched to '" + state + "'");

            switch (state) {
                case HIGH -> {
                    if (onDown != null) {
                        onDown.run();
                    }
                    if (whilePressed != null) {
                        executor.submit(whilePressedWorker);
                    }
                }
                case LOW -> {
                    if (onUp != null) {
                        onUp.run();
                    }
                }
                case UNKNOWN -> logError("Button is in State UNKNOWN");
            }
        });
    }

    /**
     * Returns the current state of the Digital State
     *
     * @return Current DigitalInput state (Can be HIGH, LOW or UNKNOWN)
     */
    public DigitalState getState() {
        return switch (digitalInput.state()) {
            case HIGH -> inverted ? DigitalState.LOW  : DigitalState.HIGH;
            case LOW  -> inverted ? DigitalState.HIGH : DigitalState.LOW;
            default   -> DigitalState.UNKNOWN;
        };
    }

    /**
     * Checks if button is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean isDown() {
        return getState() == DigitalState.HIGH;
    }

    /**
     * Checks if button is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean isUp() {
        return getState() == DigitalState.LOW;
    }

    /**
     * Returns the Pi4J DigitalInput associated with this component.
     *
     * @return Returns the Pi4J DigitalInput associated with this component.
     */
    public DigitalInput getDigitalInput() {
        return this.digitalInput;
    }

    /**
     * Builds a new DigitalInput configuration for the button component.
     *
     * @param pi4j     Pi4J context
     * @param address  GPIO address of button component
     * @param inverted Specify if button state is inverted
     * @param debounce Debounce time in microseconds
     * @return DigitalInput configuration
     */
    private DigitalInputConfig buildDigitalInputConfig(Context pi4j, PIN address, boolean inverted, long debounce) {
        return DigitalInput.newConfigBuilder(pi4j).id("BCM" + address)
                .name("Button #" + address)
                .address(address.getPin())
                .debounce(debounce).pull(inverted ? PullResistance.PULL_UP : PullResistance.PULL_DOWN)
                .build();
    }


    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onDown(Runnable task) {
        this.onDown = task;
    }

    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onUp(Runnable task) {
        this.onUp = task;
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whilePressed(Runnable task, long whilePressedDelay) {
        this.whilePressed = task;
        this.whilePressedDelay = whilePressedDelay;
    }

    /**
     * disables all the handlers for the onUp, onDown and WhilePressed Events
     */
    public void deRegisterAll() {
        this.onDown = null;
        this.onUp = null;
        this.whilePressed = null;
        this.executor.shutdown();
    }

    /**
     * Returns the methode for OnDown
     *
     * @return Runnable onDown
     */
    public Runnable getOnDown() {
        return onDown;
    }

    /**
     * Returns the methode for OnUp
     *
     * @return Runnable onUp
     */
    public Runnable getOnUp() {
        return onUp;
    }

    /**
     * Returns the methode for whilePressed
     *
     * @return Runnable whilePressed
     */
    public Runnable getWhilePressed() {
        return whilePressed;
    }
}
