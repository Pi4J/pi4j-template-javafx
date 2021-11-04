package com.pi4j.fxgl.pi4jplain;

import java.time.Duration;

import com.pi4j.fxgl.util.HardwareButton;
import com.pi4j.fxgl.util.HardwareButtonImpl;


public class HardwareButtonDemo {
    private static final int PIN_BUTTON = 24;  // BCM 24 = GPIO-PIN 18 , START-Button of Picade

    public static void main(String[] args) {

        //todo: instantiate a new HardwareButtonImpl
        // don't forget to initialize the button



        // todo: add EventHandler and print a message on console



        //todo: allow 15 seconds before you stop the app



        //todo: shutdown everything, the button and the Pi4JContext
    }

    private static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
