package com.pi4j.components.tiles;

import com.pi4j.components.interfaces.LedMatrixInterface;
import com.pi4j.components.tiles.Skins.LedMatrixSkin;
import com.pi4j.context.Context;
import com.pi4j.context.ContextConfig;
import javafx.scene.paint.Color;

import java.util.Arrays;

import static com.pi4j.components.components.LedMatrix.setMPChelper;

public class LedMatrixTile extends Pi4JTile implements LedMatrixInterface {

    /**
     * The array of all pixels
     */
    private final int[][] ledMatrix;

    private final LedMatrixSkin skin = new LedMatrixSkin(this);

    private int amountRow;

    private int amountLeds;

    /**
     * Brightness value between 0 and 1
     */
    private double brightness;

    public LedMatrixTile(Context pi4j, int[][] matrix, double brightness, int channel) {
        setText("Channel: "+ channel);
        ledMatrix = matrix;
        setAmountRow(ledMatrix.length);
        setAmountLeds(ledMatrix[0].length);
        constructorValues(brightness);

    }

    public LedMatrixTile(Context pi4j, int rows, int columns, double brightness){
        this.setAmountLeds(columns);
        this.setAmountRow(rows);
        ledMatrix = new int[amountRow][amountLeds];
        constructorValues(brightness);
    }

    public LedMatrixTile(Context pi4j, int[][] matrix, double brightness) {
        ledMatrix = matrix;
        setAmountRow(ledMatrix.length);
        setAmountLeds(ledMatrix[0].length);
        constructorValues(brightness);
    }

    /**
     * Setting all LEDs off and closing the strip
     */
    @Override
    public void close() {
        allOff();
    }

    /**
     * Rendering the LEDs by setting the pixels on the lED strip component
     */
    @Override
    public void render() {
        for (int j = 0; j < this.getAmountRow(); j++) {

            for (int i = 0; i < this.getAmountLeds(); i++) {

                //get rgb int value of each color
                int red = (ledMatrix[j][i] >> 16) & 0xff;
                int green = (ledMatrix[j][i] >> 8) & 0xff;
                int blue = ledMatrix[j][i] & 0xff;

                Color c = Color.rgb(red, green, blue);

                /* set brightness value to the opposite
                 * convert brightness value for opacity of black overlay
                 */

                double brightnessToOpacity = (brightness-1.0)*-1;

                skin.brightnessOverlay[j][i].setOpacity(brightnessToOpacity);
                skin.leds[j][i].setFill(c);
            }
        }

    }

    /**
     * setting all LEDs off
     */
    @Override
    public void allOff() {
        for (int i = 0; i < this.getAmountRow(); i++) {
            Arrays.fill(ledMatrix[i], 0);
        }
    }

    /**
     * setting the color of a specified led on the strip
     *
     * @param pixel which position on the strip, range 0 - numLEDS-1
     * @param color the color that is set
     */
    @Override
    public void setPixelColor(int strip, int pixel, int color) {
        if (strip > ledMatrix.length || strip < 0 || pixel > ledMatrix[strip].length || pixel < 0) {
            throw new IllegalArgumentException("the strip or LED specified does not exist");
        }
        ledMatrix[strip][pixel] = color;
    }

    /**
     * setting the color of a specified LED on the matrix
     *
     * @param pixelNumber which position in the matrix,
     *                    if it was laid out like a single strip
     * @param color       the color that is set
     */
    @Override
    public void setMatrixPixelColor(int pixelNumber, int color) {
        setMPChelper(pixelNumber, color, ledMatrix);
    }

    /**
     * Setting all LEDs of a row to the same color
     *
     * @param color the color that is set
     */
    @Override
    public void setStripColor(int strip, int color) {
        if (strip > ledMatrix.length || strip < 0) {
            throw new IllegalArgumentException("the strip specified does not exist");
        }
        Arrays.fill(ledMatrix[strip], color);
    }

    /**
     * Setting all LEDs in the matrix to the same color
     *
     * @param color the color that is set
     */
    @Override
    public void setMatrixColor(int color) {
        for (int[] strips : ledMatrix) {
            Arrays.fill(strips, color);
        }
    }

    public double getBrightness() {
        return this.brightness;
    }

    /**
     * Set the brightness of all LED's
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

    public int getAmountRow() {
        return amountRow;
    }

    public void setAmountRow(int amountRow) {
        if(amountRow < 2){
            this.amountRow = 2;
        } else
            this.amountRow = Math.min(amountRow, 4);
    }

    public int getAmountLeds() {
        return amountLeds;
    }

    public void setAmountLeds(int amountLeds) {
        this.amountLeds = amountLeds;
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(double brightness){
        setTitle("LED Matrix");

        //compare skin amountRow and numRow from constructor. initGraphics to defined numRow
        if(skin.getAmountRow() != amountRow){
            skin.setAmountRow(amountRow);
        }
        skin.initGraphics();
        setBrightness(brightness);
        setSkin(skin);
    }
}
