/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.pi4j.fxgl.game;

import java.util.Map;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;

import com.pi4j.fxgl.util.ArcadeConsoles;
import com.pi4j.fxgl.util.ArcadeToFXGLBridge;
import com.pi4j.fxgl.util.PicadeButton;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.run;

/**
 * This is an FXGL version of the libGDX simple game tutorial, which can be found
 * here - https://github.com/libgdx/libgdx/wiki/A-simple-game
 *
 * The player can move the bucket left and right to catch water droplets.
 * There are no win/lose conditions.
 *
 * Note: for simplicity's sake all of the code is kept in this file.
 * In addition, most of typical FXGL API is not used to avoid overwhelming
 * FXGL beginners with a lot of new concepts to learn.
 *
 * Although the code is self-explanatory, some may find the comments useful
 * for following the code.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 *
 */
public class DropApp_Solution extends GameApplication {

    /**
     * Types of entities in this game.
     */
    public enum Type {
        DROPLET, BUCKET
    }

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Drop");
        settings.setVersion("1.0");
        settings.setWidth(ArcadeConsoles.PICADE.getWidth());
        settings.setHeight(ArcadeConsoles.PICADE.getHeight());

        //use this to get some profiling information
        //settings.setProfilingEnabled(true);
    }

    @Override
    protected void initGame() {
        //Todo: Picade addition: initialize the Arcade console to get access to the Joystick and the physical buttons
        // Hint: use PicadeButton
        PicadeButton.initializeAll();

        spawnBucket();

        // creates a timer that runs spawnDroplet() every second
        run(this::spawnDroplet, Duration.seconds(1));

        // loop background music located in /resources/assets/music/
        // currently not working on pi
        //loopBGM("bgm.mp3");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.BUCKET, Type.DROPLET, (bucket, droplet) -> {

            // code in this block is called when there is a collision between Type.BUCKET and Type.DROPLET

            // remove the collided droplet from the game
            droplet.removeFromWorld();

            // play a sound effect located in /resources/assets/sounds/
            // currently not working on pi
            //play("drop.wav");
        });
    }

    /**
     * Called every frame _only_ in Play state.
     *
     * @param tpf time per frame
     */
    @Override
    protected void onUpdate(double tpf) {
        // for each entity of Type.DROPLET translate (move) it down
        getGameWorld().getEntitiesByType(Type.DROPLET).forEach(droplet -> droplet.translateY(150 * tpf));
    }

    /**
     * build an entity with Type.BUCKET
     * at the position X = getAppWidth() / 2 and Y = getAppHeight() - 200
     * with a view "bucket.png", which is an image located in /resources/assets/textures/
     * also create a bounding box from that view
     * make the entity collidable
     * finally, complete building and attach to the game world
     *
     * there's only one bucket
     */
    private void spawnBucket() {
        Entity bucket = entityBuilder()
                .type(Type.BUCKET)
                .at(getAppWidth() / 2.0, getAppHeight() - 100.0)
                .viewWithBBox("bucket.png")
                .collidable()
                .buildAndAttach();

        // bind bucket's X value to mouse X
        getInput().mouseXWorldProperty().addListener((observableValue, number, t1) -> bucket.setX(t1.doubleValue()));

        // bind cursor buttons to move the bucket
        onKey(KeyCode.LEFT,  () -> bucket.setX(bucket.getX() - 5));
        onKey(KeyCode.RIGHT, () -> bucket.setX(bucket.getX() + 5));


        //Todo: Picade addition: enable Picade's Button_1 and Button_2 to do the same as the cursor-keys on desktop
        // hint: use ArcadeToFXGLBridge.bridge
        PicadeButton.JOYSTICK.xProperty().bindBidirectional(bucket.xProperty());


        //Todo: Picade addition: bind JOYSTICK to bucket's x-position
        // hint: use JavaFX bidirectional binding
        ArcadeToFXGLBridge.bridge(Map.of(PicadeButton.Button_1, KeyCode.LEFT,
                                         PicadeButton.Button_2, KeyCode.RIGHT
                                        ));

         // Alternative if you don't like the Map
         // ArcadeToFXGLBridge.mapButtonToKeyCode(PicadeButton.Button_1, KeyCode.LEFT);
         // ArcadeToFXGLBridge.mapButtonToKeyCode(PicadeButton.Button_2, KeyCode.RIGHT);
    }

    /**
     * this is done often (every second, see initGame)
     */
    private void spawnDroplet() {
        entityBuilder()
                .type(Type.DROPLET)
                .at(FXGLMath.random(0, getAppWidth() - 64), 0)
                .viewWithBBox("droplet.png")
                .collidable()
                .with(new OffscreenCleanComponent()) //clean up when droplet disappears
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
