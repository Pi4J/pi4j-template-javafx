package com.pi4j.components.components;

import com.pi4j.context.Context;
import com.pi4j.io.spi.Spi;
import com.pi4j.components.interfaces.*;

import java.util.Arrays;

/**
 * Creates an SPI Control for Neopixel LED Strips
 */
public class LedMatrix extends Component implements LedMatrixInterface {

    /**
     * Default Channel of the SPI Pins
     */
    protected static final int DEFAULT_SPI_CHANNEL = 0;

    /**
     * The PI4J SPI
     */
    protected final Spi spi;
    /**
     * The PI4J context
     */
    protected final Context context;
    /**
     * The matrix, that includes all pixels of all LEDStrips
     */
    private final int[][] matrix;
    /**
     * The corresponding LEDStrip to the matrix. The matrix is nothing more than just an array from LEDStrips, that
     * can be calculated to a single LEDStrip
     */
    private final LedStrip ledStrip;
    /**
     * Brightness value between 0 and 1
     */
    private double brightness;
    private int numLEDs;

    /**
     * Create a new LEDMatrix with the defined Matrix.
     * You can give in something like int[3][4] or
     * matrix = {{0, 0, 0},
     * {0, 0, 0, 0},
     * {0}}
     *
     * @param pi4j       Pi4J context
     * @param matrix     How many LEDs are on this Strand
     * @param brightness How bright the LEDs can be at max, Range 0 - 1
     */
    public LedMatrix(Context pi4j, int[][] matrix, double brightness) {
        this(pi4j, matrix, brightness, DEFAULT_SPI_CHANNEL);
    }

    /**
     * Creates a new LEDMatrix with the defined rows and columns
     *
     * @param pi4j       Pi4J context
     * @param rows       How many rows of LED
     * @param columns    How many columns of LED
     * @param brightness How bright the LEDs can be at max, Range 0 - 1
     */
    public LedMatrix(Context pi4j, int rows, int columns, double brightness) {
        this(pi4j, new int[rows][columns], brightness, DEFAULT_SPI_CHANNEL);
    }

    /**
     * Creates a new LEDMatrix component with a custom BCM pin.
     *
     * @param pi4j       Pi4J context
     * @param matrix     How many LEDs are on this Strand
     * @param brightness How bright the LEDs can be at max, Range 0 - 255
     * @param channel    which channel to use
     */
    public LedMatrix(Context pi4j, int[][] matrix, double brightness, int channel) {
        this.matrix = matrix;
        this.brightness = brightness;
        this.context = pi4j;

        // Allocate SPI transmit buffer (same size as PCM)
        this.numLEDs = 0;
        for (int[] strips : matrix) {
            this.numLEDs += strips.length;
        }

        this.ledStrip = new LedStrip(pi4j, numLEDs, brightness, channel);
        this.spi = ledStrip.spi;
    }

    /**
     * @return the Pi4J context
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Setting all LEDs off and closing the strip
     */
    @Override
    public void close() {
        ledStrip.close();
    }

    /**
     * function to get the amount of the LEDs in the matrix
     *
     * @return int with the amount of LEDs over all
     */
    public int getNumPixels() {
        return numLEDs;
    }

    /**
     * function to get the amount of the LEDs in the specified strip
     *
     * @return int with the amount of LEDs over all
     */
    public int getNumPixels(int strip) {
        if (strip > matrix.length || strip < 0) {
            throw new IllegalArgumentException("the strip specified does not exist");
        }
        return matrix[strip].length;
    }

    /**
     * function to get the color (as an int) of a specified LED
     *
     * @param pixel which position on the LED strip, range 0 - numLEDS-1
     * @return the color of the specified LED on the strip
     */
    public int getPixelColor(int strip, int pixel) {
        if (strip > matrix.length || strip < 0 || pixel > matrix[strip].length || pixel < 0) {
            throw new IllegalArgumentException("the strip or led specified does not exist");
        }
        return matrix[strip][pixel];
    }

    /**
     * setting the color of a specified led on the strip
     *
     * @param pixel which position on the strip, range 0 - numLEDS-1
     * @param color the color that is set
     */
    @Override
    public void setPixelColor(int strip, int pixel, int color) {
        if (strip > matrix.length || strip < 0 || pixel > matrix[strip].length || pixel < 0) {
            throw new IllegalArgumentException("the strip or LED specified does not exist");
        }
        matrix[strip][pixel] = color;
    }

    /**
     * function to get the color (as an int) of a specified LED on the matrix
     *
     * @param pixelNumber which position in the matrix
     *                    if it was laid out like a single strip
     * @return the color of the specified led on the strip
     */
    public int getMatrixPixelColor(int pixelNumber) {
        if (pixelNumber > matrix.length || pixelNumber < 0) {
            throw new IllegalArgumentException("the strip or LED specified does not exist");
        }
        int counter = 0;
        int strip = 0;
        int LED = 0;
        while (counter < pixelNumber && strip < matrix.length) {
            LED = 0;
            while (counter < pixelNumber && LED < matrix[strip].length) {
                counter++;
                LED++;
            }
            if (counter < pixelNumber) strip++;
        }
        return matrix[strip][LED];
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
        setMPChelper(pixelNumber, color, matrix);
    }

    /**
     * Setting all LEDs of a row to the same color
     *
     * @param color the color that is set
     */
    @Override
    public void setStripColor(int strip, int color) {
        if (strip > matrix.length || strip < 0) {
            throw new IllegalArgumentException("the strip specified does not exist");
        }
        Arrays.fill(matrix[strip], color);
    }

    /**
     * Setting all LEDs in the matrix to the same color
     *
     * @param color the color that is set
     */
    @Override
    public void setMatrixColor(int color) {
        for (int[] strips : matrix) {
            Arrays.fill(strips, color);
        }
    }

    /**
     * Rendering the LEDs by setting the pixels on the lED strip component
     */
    @Override
    public void render() {
        int counter = 0;
        ledStrip.setBrightness(brightness);
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                ledStrip.setPixelColor(counter++, anInt);
            }
        }
        ledStrip.render();
    }

    /**
     * setting all LEDs off
     */
    @Override
    public void allOff() {
        ledStrip.allOff();
    }

    /**
     * @return the current brightness
     */
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
        this.brightness = brightness;
    }

    public static void setMPChelper(int pixelNumber, int color, int[][] ledMatrix) {
        if (pixelNumber > ledMatrix.length || pixelNumber < 0) {
            throw new IllegalArgumentException("the LED specified does not exist");
        }
        int counter = 0;
        int strip = 0;
        int LED = 0;
        while (counter < pixelNumber && strip < ledMatrix.length) {
            LED = 0;
            while (counter < pixelNumber && LED < ledMatrix[strip].length) {
                counter++;
                LED++;
            }
            if (counter < pixelNumber) strip++;
        }
        ledMatrix[strip][LED] = color;
    }
}
