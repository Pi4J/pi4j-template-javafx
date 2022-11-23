package com.pi4j.components.tiles;

import com.pi4j.components.components.LEDStrip;
import com.pi4j.components.interfaces.LEDStripInterface;
import com.pi4j.components.tiles.Skins.LEDStripSkin;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class LedStripTile extends Pi4JTile implements LEDStripInterface {

    /**
     * The array of all pixels
     */
    private int[] LEDs;

    private LEDStripSkin ledstrip = new LEDStripSkin(this);

    /**
     * Brightness value between 0 and 1
     */
    private double brightness;

    public LedStripTile(){
        minHeight(400);
        minWidth(400);
        setTitle("LED Strip");
        setText("Pin");
        setSkin(ledstrip);
        LEDs = new int[4];
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

            //Scaling the color to the max brightness
//            LEDs[i] = PixelColor.setRedComponent(LEDs[i], (int) (PixelColor.getRedComponent(LEDs[i]) * brightness));
//            LEDs[i] = PixelColor.setGreenComponent(LEDs[i], (int) (PixelColor.getGreenComponent(LEDs[i]) * brightness));
//            LEDs[i] = PixelColor.setBlueComponent(LEDs[i], (int) (PixelColor.getBlueComponent(LEDs[i]) * brightness));

            int red = (LEDs[i] >> 16) & 0xff;
            int green = (LEDs[i] >> 8) & 0xff;
            int blue = LEDs[i] & 0xff;

            Color c = Color.rgb(red,green,blue);

            ledstrip.leds[i].setFill(c);


            //TODO lastRenderTime?

        }

    }

    @Override
    public void allOff() {
        Arrays.fill(LEDs, 0);
        render();

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

    public class PixelColor {
        public static final int WHITE = 0xFFFFFF;
        public static final int RED = 0xFF0000;
        public static final int ORANGE = 0xFFA500;
        public static final int YELLOW = 0xFFFF00;
        public static final int GREEN = 0x00FF00;
        public static final int LIGHT_BLUE = 0xadd8e6;
        public static final int BLUE = 0x0000FF;
        public static final int PURPLE = 0x800080;
        public static final int PINK = 0xFFC0CB;

        public static final int Color_COMPONENT_MAX = 0xff;
        private static final int WHITE_MASK = 0xffffff;
        private static final int RED_MASK = 0xff0000;
        private static final int GREEN_MASK = 0x00ff00;
        private static final int BLUE_MASK = 0x0000ff;
        private static final int RED_OFF_MASK = 0x00ffff;
        private static final int GREEN_OFF_MASK = 0xff00ff;
        private static final int BLUE_OFF_MASK = 0xffff00;

        public static void validateColorComponent(String color, int value) {
            if (value < 0 || value >= 256) {
                throw new IllegalArgumentException("Illegal Color value (" + value +
                    ") for '" + color + "' - must be 0.." + Color_COMPONENT_MAX);
            }
        }

        /**
         * Get the red value of a color
         *
         * @param color provide the color
         * @return the red value
         */
        public static int getRedComponent(int color) {
            return (color & RED_MASK) >> 16;
        }

        /**
         * Set the red value of a color
         *
         * @param color provide the color
         * @param red   provide the desired red value
         * @return the new color
         */
        public static int setRedComponent(final int color, int red) {
            validateColorComponent("Red", red);
            int new_Color = color & RED_OFF_MASK;
            new_Color |= red << 16;
            return new_Color;
        }

        /**
         * Get the green value of a color
         *
         * @param color provide the color
         * @return the green value
         */
        public static int getGreenComponent(int color) {
            return (color & GREEN_MASK) >> 8;
        }

        /**
         * Set the green value of a color
         *
         * @param color provide the color
         * @param green provide the desired red value
         * @return the new color
         */
        public static int setGreenComponent(final int color, int green) {
            validateColorComponent("Green", green);
            int new_Color = color & GREEN_OFF_MASK;
            new_Color |= green << 8;
            return new_Color;
        }

        /**
         * Get the blue value of a color
         *
         * @param color provide the color
         * @return the blue value
         */
        public static int getBlueComponent(int color) {
            return color & BLUE_MASK;
        }

        /**
         * Set the blue value of a color
         *
         * @param color provide the color
         * @param blue  provide the desired red value
         * @return the new color
         */
        public static int setBlueComponent(final int color, int blue) {
            validateColorComponent("Blue", blue);
            int new_Color = color & BLUE_OFF_MASK;
            new_Color |= blue;
            return new_Color;
        }
    }

}
