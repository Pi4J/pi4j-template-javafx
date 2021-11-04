package com.pi4j.fxgl.pi4jplain;

import java.time.Duration;

import com.pi4j.fxgl.util.PicadeButton;

public class PicadeButtonDemo {
    public static void main(String[] args) {

        //todo: initialize all PicadeButtons


        //todo: add EventHandler to PicadeButton.Button_1



        //todo: use Joystick


    }

    private static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
