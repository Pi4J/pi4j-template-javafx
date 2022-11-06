package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.ButtonInterface;
import eu.hansolo.tilesfx.Tile;

public class SimpleButtonTile extends Tile implements ButtonInterface {

    public SimpleButtonTile(){
        prefHeight(400);
        prefWidth(400);
        setSkinType(SkinType.LED);
        setTitle("SimpleButton");
        setText("Separate Tile");
    }

    @Override
    public void onDown(Runnable task) {
        this.setOnMouseClicked(event -> task.run());
    }

    @Override
    public void onUp(Runnable task) {
        this.setOnMouseClicked(event -> task.run());

    }

    @Override
    public void whilePressed(Runnable task, long whilePressedDelay) {
        this.setOnMousePressed(event -> task.run());

    }

    @Override
    public void deRegisterAll() {

    }
}
