package com.pi4j.fxgl.util;

/**
 * The supported Arcade Consoles with their screen resolutions
 *
 * @author Dieter Holz
 */
public enum ArcadeConsoles {
    PICADE  (1024, 768),
    GAME_HAT( 490, 320);

    private final int width;
    private final int height;

    ArcadeConsoles(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
