package com.pi4j.fxgl.util;

/**
 * Implementation of the Picade console buttons using Pi4J
 */
public enum PicadeButton implements HardwareButton {

    // technically the joystick consists of 4 buttons
    // it's more convenient to use 'Joystick' instead of using these internal buttons
    UP   (12, "Joystick up"),
    DOWN ( 6, "Joystick down"),
    LEFT (20, "Joystick left"),
    RIGHT(16, "Joystick right"),


    //these are the real button on GameHAT
    ENTER   (27, "Enter"),
    ESCAPE  (22, "Escape"),
    COIN    (23, "Coin"),
    START   (24, "Start"),
    Button_1( 5, "Button 1"),
    Button_2(11, "Button 2"),
    Button_3( 8, "Button 3"),
    Button_4(25, "Button 4"),
    Button_5( 9, "Button 5"),
    Button_6(10, "Button 6");

    private final HardwareButton button;

    public static Joystick JOYSTICK;

    PicadeButton(int bcmPin, String label) {
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
        JOYSTICK = new Joystick(UP, DOWN, LEFT, RIGHT, ArcadeConsoles.PICADE);
    }

    public static void shutdownAll(){
        for (HardwareButton b : values()) {
            b.shutdown();
        }
    }

}
