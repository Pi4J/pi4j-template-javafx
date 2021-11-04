package com.pi4j.fxgl.game.mazelaman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;

@SuppressWarnings("unused")
public class MazelaManFactory implements EntityFactory {

    @Spawns("Background")
    public Entity spawnBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.BLACK))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .viewWithBBox("mazelaman/player.png")
                .build();
    }
}
