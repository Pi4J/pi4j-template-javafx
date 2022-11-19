package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.LEDButtonInterface;
import com.pi4j.components.tiles.Skins.LedButtonSkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LedButtonTile extends Pi4JTile implements LEDButtonInterface {

    LedButtonSkin ledButtonSkin = new LedButtonSkin(this);

    private Runnable onDown = () -> {
    };
    private Runnable onUp = () -> {
    };
    private Runnable btnwhilePressed = () -> {
    };

    private boolean isDown = false;
    private long whilePressedDelay;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Überprüft, ob der Button gedrückt ist und setzt ein Delay ein, Falls Button weiterhin gedrückt ist,
     * wird der whilePressed Runnable aktiviert.
     */
    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);
            if (isDown) {
                btnwhilePressed.run();
            }
        }
    };

    public LedButtonTile() {
        minHeight(400);
        minWidth(400);
        setTitle("LED Button");
        setText("Pin 22, Pin 24"); //TODO: change to correct way
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

    // Setzt den aktuellen Thread mit dem Wert des gegebenen Parameter (in Millisekunden) zu Schlaf
    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void LEDsetStateOn() {
        this.setActive(true);
    }

    @Override
    public void LEDsetStateOff() {
        this.setActive(false);

    }

    @Override
    public void onDown(Runnable task) {
        this.onDown = task;
    }

    @Override
    public void onUp(Runnable task) {
        this.onUp = task;
    }

    @Override
    public void btnwhilePressed(Runnable method, long millis) {
        this.btnwhilePressed = method;
        this.whilePressedDelay = millis;

    }
}
