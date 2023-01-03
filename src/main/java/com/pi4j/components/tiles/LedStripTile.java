package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.tiles.Skins.LEDStripSkin;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class LedStripTile extends Pi4JTile implements LEDStripInterface {

    /**
     * The array of all pixels
     */
    private final int[] LEDs;

    private final LEDStripSkin ledstrip = new LEDStripSkin(this);

    /**
     * Brightness value between 0 and 1
     */
    private double brightness;

    public LedStripTile(int numLEDs, double brightness){
        minHeight(400);
        minWidth(400);
        setTitle("LED Strip");
        setText("Pin");
        setSkin(ledstrip);
        LEDs = new int[numLEDs];
        setBrightness(brightness);
    }


    @Override
    public void close() {
        allOff();
    }

    @Override
    public int getPixelColor(int pixel) {
        return LEDs[pixel];
    }

    @Override
    public void setPixelColor(int pixel, int color) {
        LEDs[pixel] = color;
    }

    @Override
    public void setStripColor(int color) {
        Arrays.fill(LEDs, color);
    }


    //re-render
    @Override
    public void render() {
        for (int i = 0; i < LEDs.length; i++) {

            //get rgb int value of each color
            int red = (LEDs[i] >> 16) & 0xff;
            int green = (LEDs[i] >> 8) & 0xff;
            int blue = LEDs[i] & 0xff;

            Color c = Color.rgb(red,green,blue);

            /* set brightness value to the opposite
            * convert brightness value for opacity of black overlay
             */
            double brightnessToOpacity = (brightness-1.0)*-1;

            ledstrip.brightness[i].setOpacity(brightnessToOpacity);

            ledstrip.leds[i].setFill(c);
        }

    }

    @Override
    public void allOff() {
        Arrays.fill(LEDs, 0);
    }

    @Override
    public void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public double getBrightness() {
        return this.brightness;
    }

    @Override
    public void setBrightness(double brightness) {
        if (brightness < 0 || brightness > 1) {
            throw new IllegalArgumentException("Illegal Brightness Value. Must be between 0 and 1");
        }
        this.brightness = brightness;
    }
}
