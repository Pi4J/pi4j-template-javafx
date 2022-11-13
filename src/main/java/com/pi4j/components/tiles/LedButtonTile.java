package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.tiles.Skins.LedButtonSkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LedButtonTile extends Pi4JTile implements SimpleButtonInterface {

    Runnable onDown = () -> {
    };
    Runnable onUp = () -> {
    };
    private Runnable whilePressed = () -> {
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
                whilePressed.run();
            }
        }
    };

    public LedButtonTile() {
        minHeight(400);
        minWidth(400);
        setSkinType(SkinType.CUSTOM);
        setTitle("SimpleButton");
        setText("Bottom text");
        setSkin(new LedButtonSkin(this));

        setOnMousePressed(mouseEvent -> {

            //Run onDown Runnable, falls Wert nicht Null
            if (onDown != null) {
                onDown.run();
                isDown = true;
            }

            //Läuft whilePressedWorker Runnable, falls Wert nicht Null
            if (whilePressed != null) {
                executor.submit(whilePressedWorker);
            }
        });

        setOnMouseReleased(mouseEvent -> {
            onUp.run();
            isDown = false;
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
    public void onDown(Runnable task) {
        this.onDown = task;
    }

    @Override
    public void onUp(Runnable task) {
        this.onUp = () -> {
        };
    }

    @Override
    public void whilePressed(Runnable task, long whilePressedDelay) {
        this.whilePressed = task;
        this.whilePressedDelay = whilePressedDelay;
    }

    @Override
    public void deRegisterAll() {
    }
}
