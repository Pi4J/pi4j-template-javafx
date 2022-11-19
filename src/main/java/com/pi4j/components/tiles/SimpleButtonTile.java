package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.tiles.Skins.SimpleButtonSkin;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleButtonTile extends Pi4JTile implements SimpleButtonInterface {

    SimpleButtonSkin buttonSkin = new SimpleButtonSkin(this);

    private Runnable onDown = () -> { };
    private Runnable onUp   = () -> { };
    private Runnable whilePressed = () -> { };

    private boolean isDown = false;
    private long whilePressedDelay;

    /**
     * Überprüft, ob der Button gedrückt ist und setzt ein Delay ein, Falls Button weiterhin gedrückt ist,
     * wird der whilePressed Runnable aktiviert.
      */
    private final Runnable whilePressedWorker = () -> {
        while (isDown) {
            delay(whilePressedDelay);

            if(isDown) {
                whilePressed.run();
            }
        }
    };

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SimpleButtonTile(PIN pin) {
        prefHeight(400);
        prefWidth(400);
        setTitle("Simple Button");
        setText("Pin " + pin.getPin());
        setSkin(buttonSkin);

        buttonSkin.getButtonknob().setOnMousePressed(mouseEvent -> {

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

        buttonSkin.getButtonknob().setOnMouseReleased(mouseEvent -> {
            if(isDown) {
                onUp.run();
                isDown = false;
            }
        });

        buttonSkin.getButtonknob().setOnMouseExited(mouseEvent -> {
            if(isDown) {
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
    public void onDown(Runnable task) {
        onDown = task;
    }

    @Override
    public void onUp(Runnable task) {
        onUp = task;
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
