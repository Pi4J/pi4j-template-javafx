package com.pi4j.fxgl.util;

import java.time.Duration;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Technically a lot of joysticks used in arcade consoles consist internally of four buttons, that can be pressed or depressed.
 *
 * There can be 0, 1 or 2 buttons pressed at the same time.
 *
 * Joystick simulates a 'mouse position'.
 *
 * @author Dieter Holz
 */
public class Joystick {
    private static final Duration JOYSTICK_PULSE_MS = Duration.ofMillis(33);

    // the movement speed when joystick is not in home position
    // every JOYSTICK_PULSE_MS x or y is increased/decreased by SPEED
    private final static int SPEED = 10;

    // if joystick is in NW, NE, SW or SE position, then diagonal speed is used to change both x and y
    private final int diagonalSpeed = (int) Math.sqrt(SPEED * SPEED * 0.5);

    // joystick has to know the screen size
    private final int width;
    private final int height;

    // the buttons inside the joystick
    private final HardwareButton up;
    private final HardwareButton down;
    private final HardwareButton left;
    private final HardwareButton right;

    // simulation of 'mouse position'
    private final IntegerProperty x = new SimpleIntegerProperty();
    private final IntegerProperty y = new SimpleIntegerProperty();

    /**
     *
     * Intentionally private. If you need to support a new Joystick, define a new constant like PICADE_JOYSTICK and GAME_HAT_JOYSTICK.
     */
    Joystick(HardwareButton up, HardwareButton down, HardwareButton left, HardwareButton right, ArcadeConsoles resolution) {
        this.up     = up;
        this.down   = down;
        this.left   = left;
        this.right  = right;
        this.width  = resolution.getWidth();
        this.height = resolution.getHeight();
        setX(width / 2);
        setY(height / 2);

        // 'mouse position' handling is enabled by default
        whileNorth(this::movePosUp);
        whileSouth(this::movePosDown);
        whileWest(this::movePosLeft);
        whileEast(this::movePosRight);
        whileNorthEast(this::movePosUpRight);
        whileNorthWest(this::movePosUpLeft);
        whileSouthEast(this::movePosDownRight);
        whileSouthWest(this::movePosDownLeft);
    }

    /**
     * Action is triggered when joystick is put back in home position.
     *
     * Action is triggered only once
     *
     * @param action whatever needs to be done
     */
    public void onHome(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isDepressed() && right.isDepressed() && left.isDepressed()) {
                action.run();
            }
        };
        up.addOnReleased(completeAction);
        down.addOnReleased(completeAction);
        left.addOnReleased(completeAction);
        right.addOnReleased(completeAction);
    }

    /**
     * Action is triggered when joystick is put in north position.
     *
     * Action is triggered only once.
     *
     * @param action whatever needs to be done
     */
    public void onNorth(Runnable action){
        Runnable completeAction = () -> {
            if (up.isPressed() && down.isDepressed() && right.isDepressed() && left.isDepressed()) {
                action.run();
            }
        };
        up.addOnPressed(completeAction);
        right.addOnReleased(completeAction);
        left.addOnReleased(completeAction);
    }

    /**
     * Action is triggered when joystick is put in south position.
     *
     * Action is triggered only once.
     *
     * @param action whatever needs to be done
     */
    public void onSouth(Runnable action){
        Runnable completeAction = () -> {
            if (down.isPressed() && up.isDepressed() && right.isDepressed() && left.isDepressed()) {
                action.run();
            }
        };
        down.addOnPressed(completeAction);
        right.addOnReleased(completeAction);
        left.addOnReleased(completeAction);
    }

    /**
     * Action is triggered when joystick is put in west position.
     *
     * Action is triggered only once.
     *
     * @param action whatever needs to be done
     */
    public void onWest(Runnable action){
        Runnable completeAction = () -> {
            if (left.isPressed() && up.isDepressed() && right.isDepressed() && down.isDepressed()) {
                action.run();
            }
        };
        left.addOnPressed(completeAction);
        up.addOnReleased(completeAction);
        down.addOnReleased(completeAction);
    }

    /**
     * Action is triggered when joystick is put in east position.
     *
     * Action is triggered only once.
     *
     * @param action whatever needs to be done
     */
    public void onEast(Runnable action){
        Runnable completeAction = () -> {
            if (right.isPressed() && up.isDepressed() && left.isDepressed() && down.isDepressed()) {
                action.run();
            }
        };
        right.addOnPressed(completeAction);
        up.addOnReleased(completeAction);
        down.addOnReleased(completeAction);
    }

    /**
     * Action is triggered as long as joystick is in north position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileNorth(Runnable action){
        Runnable completeAction = () -> {
            if (up.isPressed() && down.isDepressed() && right.isDepressed() && left.isDepressed()) {
                action.run();
            }
        };
        up.addWhileDown(completeAction, JOYSTICK_PULSE_MS);
    }

    /**
     * Action is triggered as long as joystick is in south position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileSouth(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isPressed() && right.isDepressed() && left.isDepressed()) {
                action.run();
            }
        };
        down.addWhileDown(completeAction, JOYSTICK_PULSE_MS);
    }

    /**
     * Action is triggered as long as joystick is in west position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileWest(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isDepressed() && right.isDepressed() && left.isPressed()) {
                action.run();
            }
        };
        left.addWhileDown(completeAction, JOYSTICK_PULSE_MS);
    }

    /**
     * Action is triggered as long as joystick is in east position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileEast(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isDepressed() && right.isPressed() && left.isDepressed()) {
                action.run();
            }
        };
        right.addWhileDown(completeAction, JOYSTICK_PULSE_MS);
    }

    /**
     * Action is triggered as long as joystick is in NE position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileNorthEast(Runnable action){
        Runnable completeAction = () -> {
            if (up.isPressed() && down.isDepressed() && right.isPressed() && left.isDepressed()) {
                action.run();
            }
        };
        up.addWhileDown(completeAction, JOYSTICK_PULSE_MS, right);
    }

    /**
     * Action is triggered as long as joystick is in NW position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileNorthWest(Runnable action){
        Runnable completeAction = () -> {
            if (up.isPressed() && down.isDepressed() && right.isDepressed() && left.isPressed()) {
                action.run();
            }
        };
        up.addWhileDown(completeAction, JOYSTICK_PULSE_MS, left);
    }

    /**
     * Action is triggered as long as joystick is in SW position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileSouthWest(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isPressed() && right.isDepressed() && left.isPressed()) {
                action.run();
            }
        };
        down.addWhileDown(completeAction, JOYSTICK_PULSE_MS, left);
    }

    /**
     * Action is triggered as long as joystick is in SE position.
     *
     * Action is triggered every JOYSTICK_PULSE_MS milliseconds.
     *
     * @param action whatever needs to be done
     */
    public void whileSouthEast(Runnable action){
        Runnable completeAction = () -> {
            if (up.isDepressed() && down.isPressed() && right.isPressed() && left.isDepressed()) {
                action.run();
            }
        };
        down.addWhileDown(completeAction, JOYSTICK_PULSE_MS, right);
    }

    /**
     * Move the 'mouse' to the right by 'speed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x property is visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x has to be done in UI-thread.
     */
    private void movePosRight() {
        Platform.runLater(() -> setX(Math.min(width, getX() + SPEED)));
    }

    /**
     * Move the 'mouse' to the left by 'speed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x property is visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x has to be done in UI-thread.
     */
    private void movePosLeft() {
        Platform.runLater(() -> setX(Math.max(0, getX() - SPEED)));
    }

    /**
     * Move the 'mouse' down by 'speed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the y property is visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of y has to be done in UI-thread.
     */
    private void movePosDown() {
        Platform.runLater(() -> setY(Math.min(height, getY() + SPEED)));
    }

    /**
     * Move the 'mouse' down by 'speed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the y property is visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of y has to be done in UI-thread.
     */
    private void movePosUp() {
        Platform.runLater(() -> setY(Math.max(0, getY() - SPEED)));
    }

    /**
     * Move the 'mouse' diogonal by 'diagonalSpeed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x and y properties are visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x and y has to be done in UI-thread.
     */
    private void movePosDownRight() {
        Platform.runLater(() -> {
            setY(Math.min(height, getY() + diagonalSpeed));
            setX(Math.min(width, getX() + diagonalSpeed));
        });
    }

    /**
     * Move the 'mouse' diogonal by 'diagonalSpeed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x and y properties are visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x and y has to be done in UI-thread.
     */
    private void movePosDownLeft() {
        Platform.runLater(() -> {
            setY(Math.min(height, getY() + diagonalSpeed));
            setX(Math.max(0, getX() - diagonalSpeed));
        });
    }

    /**
     * Move the 'mouse' diogonal by 'diagonalSpeed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x and y properties are visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x and y has to be done in UI-thread.
     */
    private void movePosUpRight() {
        Platform.runLater(() -> {
            setY(Math.max(0, getY() - diagonalSpeed));
            setX(Math.min(width, getX() + diagonalSpeed));
        });
    }

    /**
     * Move the 'mouse' diogonal by 'diagonalSpeed' number of 'pixels' but keep it inside the screen size.
     *
     * Typically the x and y properties are visualized in a JavaFX-UI (FXGL is using JavaFX).
     * Therefore the change of x and y has to be done in UI-thread.
     */
    private void movePosUpLeft(){
        Platform.runLater(() -> {
            setY(Math.max(0, getY() - diagonalSpeed));
            setX(Math.max(0, getX() - diagonalSpeed));
        });
    }


    // getter and setter method for the 'mouse position'
    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }
}
