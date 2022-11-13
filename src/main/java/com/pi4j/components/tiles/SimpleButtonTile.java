package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.tiles.Skins.SimpleButtonSkin;

public class SimpleButtonTile extends Pi4JTile implements SimpleButtonInterface {

    private Runnable onDown = () -> { };
    private Runnable onUp   = () -> { };

    public SimpleButtonTile(PIN pin){
        prefHeight(400);
        prefWidth(400);
        setTitle("Simple Button");
        setText("Pin " + pin.getPin());
        setSkin(new SimpleButtonSkin(this));

        setOnMousePressed(mouseEvent -> onDown.run());
        setOnMouseReleased(mouseEvent -> onUp.run());
    }


    @Override
    public void onDown(Runnable task) {
        onDown = task;
    }

    @Override
    public void onUp(Runnable task) {
        onUp = task;
    }

    //das hier analog zu onUp/Down anpassen
    @Override
    public void whilePressed(Runnable task, long whilePressedDelay) {
        this.setOnMousePressed(event -> task.run());

    }

    @Override
    public void deRegisterAll() {

    }
}
