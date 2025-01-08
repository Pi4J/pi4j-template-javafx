package com.pi4j.catalog.components;

import java.time.Duration;

import com.pi4j.context.Context;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;

import com.pi4j.catalog.components.base.Component;
import com.pi4j.catalog.components.base.PIN;

import static com.pi4j.io.gpio.digital.DigitalInput.DEFAULT_DEBOUNCE;

/**
 * Implementation of a button with integrated LED using GPIO with Pi4J.
 */
public class LedButton extends Component  {
    /**
     * Button component
     */
    private final SimpleButton button;
    /**
     * LED component
     */
    private final SimpleLed led;

    /**
     * Creates a new button component
     *
     * @param pi4j   Pi4J context
     * @param buttonAddress  GPIO address of button
     * @param inverted Specify if button state is inverted
     * @param ledAddress  GPIO address of LED
     */
    public LedButton(Context pi4j, PIN buttonAddress, Boolean inverted, PIN ledAddress) {
        this(pi4j, buttonAddress, inverted, ledAddress, DEFAULT_DEBOUNCE);
    }

    /**
     * Creates a new button component with custom GPIO address and debounce time.
     *
     * @param pi4j     Pi4J context
     * @param buttonAddress  GPIO address of button
     * @param inverted Specify if button state is inverted
     * @param ledAddress  GPIO address of LED
     * @param debounce Debounce time in microseconds
     */
    public LedButton(Context pi4j, PIN buttonAddress, boolean inverted, PIN ledAddress, long debounce) {
        this(new SimpleButton(pi4j, buttonAddress, inverted, debounce),
             new SimpleLed(pi4j, ledAddress));
    }

    public LedButton(SimpleButton button, SimpleLed led){
        this.button = button;
        this.led = led;
    }


    /**
     * Sets the LED to on.
     */
    public void ledOn() {
        led.on();
    }

    /**
     * Sets the LED to off
     */
    public void ledOff() {
        led.off();
    }

    /**
     * Toggle the LED state depending on its current state.
     *
     * @return Return true or false according to the new state of the relay.
     */
    public boolean toggleLed() {
        return led.toggle();
    }

    /**
     * Checks if button is currently pressed
     *
     * @return True if button is pressed
     */
    public boolean isDown() {
        return button.isDown();
    }

    /**
     * Checks if button is currently depressed (= NOT pressed)
     *
     * @return True if button is depressed
     */
    public boolean isUp() {
        return button.isUp();
    }

    /**
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onDown(Runnable task) {
        button.onDown(task);
    }

    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void onUp(Runnable task) {
        button.onUp(task);
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    public void whilePressed(Runnable task, Duration delay) {
        button.whilePressed(task, delay);
    }

    @Override
    public void reset(){
        button.reset();
        led.reset();
    }

    // --------------- for testing --------------------

    public MockDigitalOutput mockLed() {
        return led.mock();
    }

    public MockDigitalInput mockButton() {
        return button.mock();
    }

}