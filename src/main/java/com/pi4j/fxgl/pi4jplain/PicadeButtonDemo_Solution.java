package com.pi4j.fxgl.pi4jplain;

import java.time.Duration;

import com.pi4j.fxgl.util.Pi4JContext;
import com.pi4j.fxgl.util.PicadeButton;

public class PicadeButtonDemo_Solution {
    public static void main(String[] args) {

        //todo: initialize all PicadeButtons
        PicadeButton.initializeAll();

        //todo: add EventHandler to PicadeButton.Button_1
        PicadeButton.Button_1.addOnPressed(() -> System.out.println("pressed"));

        //todo: use Joystick
        PicadeButton.JOYSTICK.onNorth(() -> System.out.println("North"));
        PicadeButton.JOYSTICK.onHome(() -> System.out.println("Home"));
        PicadeButton.JOYSTICK.xProperty().addListener((observableValue, oldValue, newValue) -> System.out.println("x : " + newValue.doubleValue()));

        //todo: allow 15 seconds before you stop the app
        delay(Duration.ofSeconds(30));

        //todo: shutdown everything
        PicadeButton.shutdownAll();
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
