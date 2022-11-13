package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.SimpleButtonInterface;
import com.pi4j.components.tiles.Skins.LedButtonSkin;

public class LedButtonTile extends Pi4JTile implements SimpleButtonInterface {

    public LedButtonTile(){
        minHeight(400);
        minWidth(400);
        setSkinType(SkinType.CUSTOM);
        setTitle("SimpleButton");
        setText("Bottom text");
        setSkin(new LedButtonSkin(this));

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
