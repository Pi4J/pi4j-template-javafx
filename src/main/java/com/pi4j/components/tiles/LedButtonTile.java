package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.ButtonInterface;
import com.pi4j.components.tiles.Skins.LedButtonSkin;
import com.pi4j.mvc.tilesapp.controller.SomeController;
import eu.hansolo.tilesfx.Tile;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LedButtonTile extends Tile implements ButtonInterface {

    private EventHandler<MouseEvent> mouseHandler;
    public SomeController controller;

    public LedButtonTile(){
        minHeight(400);
        minWidth(400);
        setSkinType(SkinType.CUSTOM);
        setTitle("SimpleButton");
        setText("Bottom text");
        setSkin(new LedButtonSkin(this));

        //TODO: add click function to Tile class instead of Tile skin
        mouseHandler = event -> {
            final javafx.event.EventType<? extends MouseEvent> TYPE = event.getEventType();
            if (MouseEvent.MOUSE_CLICKED.equals(TYPE)) {
                this.setActive(!this.isActive());
            }
        };
    }


    @Override
    public void onDown(Runnable task) {
        this.setOnMouseClicked(event -> task.run());
    }

    @Override
    public void onUp(Runnable task) {

    }

    @Override
    public void whilePressed(Runnable task, long whilePressedDelay) {

    }

    @Override
    public void deRegisterAll() {

    }
}
