package com.pi4j.fxgl.game.mazelaman;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

public class MazelaManApp extends GameApplication {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("MazelaMan");
        settings.setVersion("chapter 1");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new MazelaManFactory());
        FXGL.spawn("Background", new SpawnData(0, 0).put("width", WIDTH).put("height", HEIGHT));
        FXGL.setLevelFromMap("mazelaman/level1-start.tmx");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
