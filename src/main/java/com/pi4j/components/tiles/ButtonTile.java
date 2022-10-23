package com.pi4j.components.tiles;

import eu.hansolo.tilesfx.Tile;

public class ButtonTile extends Tile {

    public ButtonTile(){
        prefHeight(400);
        prefWidth(400);
        setSkinType(SkinType.CUSTOM);
        setTitle("SimpleButton");

        setText("Bottom text");
    }
}
