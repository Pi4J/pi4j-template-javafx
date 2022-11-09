package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.ButtonInterface;
import eu.hansolo.tilesfx.Tile;

public class SimpleButtonTile extends Pi4JTile implements ButtonInterface {

    private Runnable onDown = () -> { };
    private Runnable onUp   = () -> { };

    public SimpleButtonTile(){
        prefHeight(400);
        prefWidth(400);
        setSkinType(SkinType.LED);
        setTitle("SimpleButton");
        setText("Separate Tile");

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
