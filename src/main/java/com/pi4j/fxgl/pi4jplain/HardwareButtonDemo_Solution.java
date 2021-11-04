package com.pi4j.fxgl.pi4jplain;

import java.time.Duration;

import com.pi4j.fxgl.util.HardwareButton;
import com.pi4j.fxgl.util.HardwareButtonImpl;
import com.pi4j.fxgl.util.Pi4JContext;

public class HardwareButtonDemo_Solution {
    private static final int PIN_BUTTON = 24;  // BCM 24 = GPIO-PIN 18 , START-Button of Picade

    public static void main(String[] args) {

        //todo: instantiate a new HardwareButtonImpl
        // don't forget to initialize the button
        HardwareButton button = new HardwareButtonImpl(PIN_BUTTON, "Start");

        button.initialize();

        // todo: add EventHandler and print a message on console
        button.addOnPressed(()  -> System.out.println(button.getLabel() + " pressed"));
        button.addOnReleased(() -> System.out.println(button.getLabel() + " released"));
        button.addWhileDown(()  -> System.out.println(button.getLabel() + " down"), Duration.ofMillis(200));

        //todo: allow 15 seconds before you stop the app
        delay(Duration.ofSeconds(15));

        //todo: shutdown everything, the button and the Pi4JContext
        button.shutdown();
        Pi4JContext.shutdown();
    }

    private static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
