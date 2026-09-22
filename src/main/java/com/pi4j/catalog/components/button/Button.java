package com.pi4j.catalog.components.button;


import java.time.Duration;


public interface Button  {
    ButtonEventHelper getEventHelper();

    /**
     * Checks if the button is currently pressed
     * <p>
     * For a not-inverted button this means: if the button is pressed, then full voltage is present
     * at the GPIO-Pin. Therefore, the DigitalState is HIGH
     *
     * @return true if the button is pressed
     */
    boolean isPressed();


    /**
     * Wiring for pull-down resistor
     *
     * [GPIO Pin]----(Button)----[3.3V Pin]
     *           |
     *          (10kΩ resistor)
     *           |
     *          [GND Pin]
     *
     * When the button is pressed, GPIO sees HIGH (3.3 V).
     * When released, the resistor pulls GPIO to LOW (GND).
     *
     * Wiring for pull-up resistor
     *
     * [GPIO Pin]----(Button)----[GND Pin]
     *           |
     *          (10kΩ resistor)
     *           |
     *          [3.3V Pin]
     *
     *
     * When the button is pressed, GPIO is shorted to GND (LOW).
     * When released, the resistor pulls GPIO up to HIGH.
     *
     * @return true if you use a 10kOhm resistor in as a pull-up resistor
     */
    boolean hasPullUpResistor();

    /**
     * Checks if the button is currently depressed (= NOT pressed)
     * <P>
     * For a not-inverted button this means: if the button is depressed, then no voltage is present
     * at the GPIO-Pin. Therefore, the DigitalState is LOW
     *
     * @return true if the button is depressed
     */
    default boolean isUp() {
        return !isPressed();
    }

    /**
     * Sets or disables the handler for the onDown event.
     * <P>
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    default void onDown(Runnable task){
        getEventHelper().onDown(task);
    }

    /**
     * Sets or disables the handler for the onUp event.
     * <P>
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    default void onUp(Runnable task){
        getEventHelper().onUp(task);
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
    default void whilePressed(Runnable task, Duration delay){
        getEventHelper().whilePressed(task, delay);
    }

    default void shutdown() {
        getEventHelper().shutdown();
    }

}
