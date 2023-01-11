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
    private Runnable btnwhilePressed = () -> {
    };

    private boolean isDown = false;
    private long whilePressedDelay;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Checks if button is pressed and runs delay function.
    // If button still is pressed, whilePressed is getting active
    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);
            if (isDown) {
                btnwhilePressed.run();
            }
        }
    };

    public LedButtonTile(Context pi4J, PIN pin1, PIN pin2) {
        constructorValues(pin1,pin2);
    }

    public LedButtonTile(Context pi4J,PIN pin1, PIN pin2, long debounce) {
        constructorValues(pin1,pin2);
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
        this.btnwhilePressed = method;
        this.whilePressedDelay = millis;

    }

    public void constructorValues(PIN pin1, PIN pin2){
        minHeight(400);
        minWidth(400);
        setTitle("LED Button");
        setText("Pin "+ pin1.getPin()+", "+pin2.getPin());
        setSkin(ledButtonSkin);


        ledButtonSkin.getLed().setOnMousePressed(mouseEvent -> {

            //Run onDown Runnable, falls Wert nicht Null
            if (onDown != null) {
                onDown.run();
                isDown = true;
            }

            //Läuft whilePressedWorker Runnable, falls Wert nicht Null
            if (btnwhilePressed != null) {
                executor.submit(whilePressedWorker);
            }
        });

        ledButtonSkin.getLed().setOnMouseReleased(mouseEvent -> {
            if (isDown) {
                onUp.run();
                isDown = false;
            }
        });

        ledButtonSkin.getLed().setOnMouseExited(mouseEvent -> {
            if (isDown) {
                onUp.run();
                isDown = false;
            }
        });
    }
}
