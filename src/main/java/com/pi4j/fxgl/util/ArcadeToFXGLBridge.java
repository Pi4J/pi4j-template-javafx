package com.pi4j.fxgl.util;

import java.util.Map;

import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getExecutor;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

/**
 * Attach arcade console to an existing FXGL Game
 *
 * @author Dieter Holz
 */
public class ArcadeToFXGLBridge {

    /**
     * Whenever a console button is touched, submit the KeyCode to trigger a FXGL action
     *
     * In play-state of FXGL game pressing the hardware button will trigger exactly the same action as pressing the key in desktop environment.
     *
     * @param button the HardwareButton of Arcade Console
     * @param keyCode the KeyCode that is used on desktop
     */
    public static void mapButtonToKeyCode(HardwareButton button, KeyCode keyCode){
        button.addOnPressed(()  -> getExecutor().startAsyncFX(() -> getInput().mockKeyPress(keyCode)));
        button.addOnReleased(() -> getExecutor().startAsyncFX(() -> getInput().mockKeyRelease(keyCode)));
    }

    /**
     * Maps all the HardwareButtons to the corresponding keyCodes
     *
     * @param allMappings contains all the necessary mappings from HardwareButton to KeyCode
     */
    public static void bridge(Map<HardwareButton, KeyCode> allMappings) {
        allMappings.forEach(ArcadeToFXGLBridge::mapButtonToKeyCode);
    }

    /**
     * Triggers action whenever the button is pressed
     *
     * @param button the arcade console button
     * @param action whatever needs to be done
     */
    public static void mapButtonToAction(HardwareButton button, Runnable action) {
        button.addOnPressed(() -> getExecutor().startAsyncFX(action));
    }
}
