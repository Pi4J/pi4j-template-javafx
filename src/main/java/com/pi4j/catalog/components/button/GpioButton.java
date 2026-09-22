package com.pi4j.catalog.components.button;

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
public class GpioButton extends DigitalSensor implements Button {

    private final ButtonEventHelper eventHelper;

    /**
     * Specifies if the button state is inverted, e.g., HIGH = depressed, LOW = pressed
     * This will also automatically switch the pull resistance to PULL_UP
     */
    private final boolean hasPullUpResistor;

    /**
     * Create a new button component that uses a pull-down resistor and a default debounce
     *
     * @param pi4j Pi4J context
     * @param address GPIO address of button
     */
    public GpioButton(Context pi4j, PIN address) {
        this(pi4j, address, false, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component
     *
     * @param pi4j Pi4J context
     * @param address  GPIO address of button
     * @param hasPullUpResistor must be 'true' if a pull-up-resistor is used
     */
    public GpioButton(Context pi4j, PIN address, boolean hasPullUpResistor) {
        this(pi4j, address, hasPullUpResistor, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component with a custom BCM-Pin and debounce time.
     *
     * @param pi4j     Pi4J context
     * @param address  GPIO address of button
     * @param hasPullUpResistor Specify if the button is wired up using a pull-up resistor
     * @param debounce Debounce time in microseconds
     */
    public GpioButton(Context pi4j, PIN address, boolean hasPullUpResistor, long debounce) {
        super(pi4j,
              DigitalInput.newConfigBuilder(pi4j)
                      .id("BCM" + address)
                      .name("Button #" + address)
                      .bcm(address.getPin())
                      .debounce(debounce)
                      .pull(hasPullUpResistor ? PullResistance.PULL_UP : PullResistance.PULL_DOWN)
                      .build());

        eventHelper = new ButtonEventHelper(this);

        this.hasPullUpResistor = hasPullUpResistor;

        /*
         * Gets a DigitalStateChangeEvent directly from the Provider, as this
         * Class is a listener. This runs in a different Thread than 'main'.
         * Calls the methods onUp, onDown and whilePressed. WhilePressed gets
         * executed in an own Thread, as to not block other resources.
         */
        digitalInput.addListener(stateChangeEvent -> eventHelper.onStateChange(stateChangeEvent.state()));

        logDebug("Created new GpioButton component on pin %s", address);
    }

    @Override
    public ButtonEventHelper getEventHelper() {
        return eventHelper;
    }

    /**
     * Checks if the button is currently pressed.
     * <P>
     * For a button wired with a pull-down resistor this means: if the button is pressed, then full voltage is present
     * at the GPIO-Pin. Therefore, the DigitalState is HIGH
     *
     * @return true if the button is pressed
     */
    public boolean isPressed() {
        return digitalInput.state() == (hasPullUpResistor ? DigitalState.LOW : DigitalState.HIGH);
    }

    @Override
    public boolean hasPullUpResistor() {
        return hasPullUpResistor;
    }

    public boolean isInInitialState(){
        return getEventHelper().isInInitialState();
    }

    /**
     * disables all the handlers for the onUp, onDown and WhileDown Events
     */
    @Override
    public void shutdown() {
        eventHelper.shutdown();
        super.shutdown();
    }


}
