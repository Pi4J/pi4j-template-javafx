package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.tiles.Skins.SimpleButtonSkin;
import com.pi4j.context.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleButtonTile extends Pi4JTile implements SimpleButtonInterface {

    SimpleButtonSkin buttonSkin             = new SimpleButtonSkin(this);

    private Runnable onDown                 = () -> { };
    private Runnable onUp                   = () -> { };
    private Runnable whilePressed           = () -> { };

    private boolean isDown                  = false;

    private final ExecutorService executor  = Executors.newSingleThreadExecutor();

    private long whilePressedDelay;

    // Checks if button is pressed and runs delay function.
    // If button still is pressed, whilePressed is getting active
    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);

            if(isDown) {
                whilePressed.run();
            }
        }
    };

    public SimpleButtonTile(Context pi4j, PIN address, boolean inverted) {
        constructorValues(address);
    }

    public SimpleButtonTile(Context pi4j, PIN address, boolean inverted, long debounce){
        constructorValues(address);
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
     * Sets or disables the handler for the onDown event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void onDown(Runnable task) {
        onDown = task;
    }

    /**
     * Sets or disables the handler for the onUp event.
     * This event gets triggered whenever the button is no longer pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void onUp(Runnable task) {
        onUp = task;
    }

    /**
     * Sets or disables the handler for the whilePressed event.
     * This event gets triggered whenever the button is pressed.
     * Only a single event handler can be registered at once.
     *
     * @param task Event handler to call or null to disable
     */
    @Override
    public void whilePressed(Runnable task, long whilePressedDelay) {
        this.whilePressed = task;
        this.whilePressedDelay = whilePressedDelay;

    }

    /**
     * disables all the handlers for the onUp, onDown and WhilePressed Events
     */
    @Override
    public void deRegisterAll() {
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(PIN pin){
        setTitle("Simple Button");
        setText("Pin " + pin.getPin());
        setSkin(buttonSkin);

        buttonSkin.getButtonknob().setOnMousePressed(mouseEvent -> {

            //Runs onDown Runnable, if value is not null
            if (onDown != null) {
                onDown.run();
                isDown = true;
            }

            //Runs whilePressedWorker Runnable, if value is not null
            if (whilePressed != null) {
                executor.submit(whilePressedWorker);
            }
        });

        // If mouse released and button down, run onUp runnable
        buttonSkin.getButtonknob().setOnMouseReleased(mouseEvent -> {
            if(isDown) {
                onUp.run();
                isDown = false;
            }
        });

        // If mouse released and button down, run onUp runnable
        buttonSkin.getButtonknob().setOnMouseExited(mouseEvent -> {
            if(isDown) {
                onUp.run();
                isDown = false;
            }
        });
    }

}
