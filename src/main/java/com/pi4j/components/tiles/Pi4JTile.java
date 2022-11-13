package com.pi4j.components.tiles;

import eu.hansolo.tilesfx.Tile;

public class Pi4JTile extends Tile {

    //workaround some TilesFX bug

    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("/tilesfx.css").toExternalForm();
    }

}
