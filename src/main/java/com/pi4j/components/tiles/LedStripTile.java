package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.tiles.Skins.LEDStripSkin;
import com.pi4j.context.Context;
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

    public LedStripTile(Context pi4j, int numLEDs, double brightness, int channel){
        constructorValues(brightness);
        setText("Channel: "+channel);
        LEDs = new int[numLEDs];

    }

    public LedStripTile(Context pi4j, int numLEDs, double brightness, int[] leDs) {
        constructorValues(brightness);
        LEDs = new int[numLEDs];

    }

    /**
     * Setting all LEDS off and closing the strip
     */
    @Override
    public void close() {
        allOff();
    }

    /**
     * function to get the color (as an int) of a specified led
     *
     * @param pixel which position on the ledstrip, range 0 - numLEDS-1
     * @return the color of the specified led on the strip
     */
    @Override
    public int getPixelColor(int pixel) {
        return LEDs[pixel];
    }

    /**
     * setting the color of a specified led on the strip
     *
     * @param pixel which position on the strip, range 0 - numLEDS-1
     * @param color the color that is set
     */
    @Override
    public void setPixelColor(int pixel, int color) {
        LEDs[pixel] = color;
    }

    /**
     * Setting all leds to the same color
     *
     * @param color the color that is set
     */
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

    /**
     * setting all LEDs off
     */
    @Override
    public void allOff() {
        Arrays.fill(LEDs, 0);
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be caught and ignored while setting the
     * interrupt flag again.
     */
    @Override
    public void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @return the current brightness
     */
    @Override
    public double getBrightness() {
        return this.brightness;
    }

    /**
     * Set the brightness of all LEDs
     *
     * @param brightness new max. brightness, range 0 - 1
     */
    @Override
    public void setBrightness(double brightness) {
        if (brightness < 0 || brightness > 1) {
            throw new IllegalArgumentException("Illegal Brightness Value. Must be between 0 and 1");
        }
        this.brightness = brightness;
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(double brightness){
        setTitle("LED Strip");
        setSkin(ledstrip);
        setBrightness(brightness);
    }
}
