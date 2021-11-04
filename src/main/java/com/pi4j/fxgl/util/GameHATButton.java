package com.pi4j.fxgl.util;

/**
 * Implementation of the GameHAT buttons using Pi4J
 */
public enum GameHATButton implements HardwareButton {

    // technically the joystick consists of 4 buttons
    // it's more convenient to use 'Joystick' instead of using these internal buttons
    UP   ( 5, "Joystick up"),
    DOWN ( 6, "Joystick down"),
    LEFT (13, "Joystick left"),
    RIGHT(19, "Joystick right"),


    //these are the real button on GameHAT
    START (21, "Start"),
    SELECT( 4, "Select"),
    TL    (23, "TL"),
    TR    (18, "TR"),
    X     (16, "X"),
    Y     (20, "Y"),
    A     (26, "A"),
    B     (12, "B");

    public static Joystick JOYSTICK;

    private final HardwareButton button;

    GameHATButton(int bcmPin, String label) {
        button = new HardwareButtonImpl(bcmPin, label);
    }

    @Override
    public HardwareButton getButtonDelegate() {
        return button;
    }

    /**
     * This must be called before one of the GameHAT buttons used.
     *
     * Initializes all the buttons and the joystick
     */
    public static void initializeAll(){
        for(HardwareButton b  : values()){
            b.initialize();
        }
        JOYSTICK = new Joystick(UP, DOWN, LEFT, RIGHT, ArcadeConsoles.GAME_HAT);
    }
}
