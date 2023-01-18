package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.LEDButtonInterface;
import com.pi4j.components.tiles.Skins.LedButtonSkin;
import com.pi4j.context.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LedButtonTile extends Pi4JTile implements LEDButtonInterface {

    LedButtonSkin ledButtonSkin = new LedButtonSkin(this);

    private Runnable onDown          = () -> {
    };
    private Runnable onUp            = () -> {
    };
    private Runnable btnWhilePressed = () -> {
    };

    private boolean isDown           = false;

    private long whilePressedDelay;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Checks if button is pressed and runs delay function.
    // If button still is pressed, whilePressed is getting active
    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);
            if (isDown) {
                btnWhilePressed.run();
            }
        }
    };

    public LedButtonTile(Context pi4j, PIN buttonaddress, Boolean inverted, PIN ledaddress) {
        constructorValues(buttonaddress,ledaddress);
    }

    public LedButtonTile(Context pi4j, PIN buttonaddress, boolean inverted, PIN ledaddress, long debounce) {
        constructorValues(buttonaddress,ledaddress);
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be catched and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Sets the LED to on.
     */
    @Override
    public void LEDsetStateOn() {
        this.setActive(true);
    }

    /**
     * Sets the LED to off.
     */
    @Override
    public void LEDsetStateOff() {
        this.setActive(false);

    }

    /**
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void onDown(Runnable task) {
        this.onDown = task;
    }

    /**
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void onUp(Runnable task) {
        this.onUp = task;
    }

    /**
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void btnwhilePressed(Runnable method, long millis) {
        this.btnWhilePressed = method;
        this.whilePressedDelay = millis;
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(PIN buttonadress, PIN ledaddress){
        setTitle("LED Button");
        setText("Pin "+ buttonadress.getPin()+", "+ledaddress.getPin());
        setSkin(ledButtonSkin);


        ledButtonSkin.getLed().setOnMousePressed(mouseEvent -> {

            // Run onDown Runnable, if values is not null
            if (onDown != null) {
                onDown.run();
                isDown = true;
            }

            // Run whilePressedWorker Runnable, if values is not null
            if (btnWhilePressed != null) {
                executor.submit(whilePressedWorker);
            }
        });

        // If mouse released and button down, run onUp runnable
        ledButtonSkin.getLed().setOnMouseReleased(mouseEvent -> {
            if (isDown) {
                onUp.run();
                isDown = false;
            }
        });

        // if mouse exited button and button down, run onUp runnable
        ledButtonSkin.getLed().setOnMouseExited(mouseEvent -> {
            if (isDown) {
                onUp.run();
                isDown = false;
            }
        });
    }
}
