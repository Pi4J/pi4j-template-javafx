package com.pi4j.fxgl.game.mazelaman_solution;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

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

    @Spawns("CherrySpawnPoint")
    public Entity spawnCherrySpawnPoint(SpawnData data) {
        return FXGL.entityBuilder(data)
                .with(new CherrySpawnComponent())
                .build();
    }

    @Spawns("Cherry")
    public Entity spawnCherry(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.CHERRY)
                .viewWithBBox("mazelaman/cherry.png")
                .collidable()
                .build();
    }

    @Spawns("Pill")
    public Entity spawnPill(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.PILL)
                .view("mazelaman/pill.png")
                // Make hit box a little smaller than the tile to fit the visible part of the image
                .bbox(new HitBox("PILL_HIT_BOX", new Point2D(5, 5), BoundingShape.box(9, 9)))
                .collidable()
                .build();
    }

    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().friction(0).density(0.1f));
        BodyDef bd = new BodyDef();
        bd.setFixedRotation(true);
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .with(physics)
                .with(new PlayerComponent(data.getX(), data.getY()))
                .collidable()
                .build();
    }

    @Spawns("Ghost")
    public Entity spawnGhost(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.GHOST)
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .with(new GhostComponent(data.get("name"), data.getX(), data.getY()))
                .collidable()
                .build();
    }

    @Spawns("Wall")
    public Entity spawnWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }
}
