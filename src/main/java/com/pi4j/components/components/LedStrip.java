package com.pi4j.components.components;

import com.pi4j.context.Context;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiConfig;
import com.pi4j.io.spi.SpiMode;

import java.util.Arrays;

/**
 * Creates an SPI Control for a Neopixel LED Strip
 */
public class LedStrip extends Component implements com.pi4j.components.interfaces.LEDStripInterface {
    /**
     * Default Channel of the SPI Pins
     */
    protected static final int DEFAULT_SPI_CHANNEL = 0;
    /**
     * Minimum time to wait for reset to occur in nanoseconds.
     */
    private static final int LED_RESET_WAIT_TIME = 300_000;
    /**
     * The PI4J SPI
     */
    protected final Spi spi;
    /**
     * The PI4J context
     */
    protected final Context context;
    /**
     * The amount of all LEDs
     */
    private final int numLEDs;
    /**
     * Default frequency of a WS2812 Neopixel Strip
     */
    private final int frequency = 800_000;
    /**
     * between each rendering of the strip, there has to be a reset-time where nothing is written to the SPI
     */
    private final int renderWaitTime;
    /**
     * The array of all pixels
     */
    private final int[] LEDs;
    /**
     * The raw-data of all pixels, each int of LEDs is split into bits and converted to bytes to write
     */
    private final byte[] pixelRaw;
    /**
     * the conversion from bit's of an integer to a byte we can write on the SPI
     */
    private final byte Bit_0 = (byte) 0b11000000;// 192 in Decimal
    private final byte Bit_1 = (byte) 0b11111000;// 248 in Decimal
    private final byte Bit_Reset = (byte) 0b00000000;// 0 in Decimal
    /**
     * Brightness value between 0 and 1
     */
    private double brightness;
    /**
     * The time, when the last rendering happened
     */
    private long lastRenderTime;

    /**
     * Creates a new simpleLed component with a custom BCM pin.
     *
     * @param pi4j       Pi4J context
     * @param numLEDs    How many LEDs are on this Strand
     * @param brightness How bright the leds can be at max, Range 0 - 255
     */
    public LedStrip(Context pi4j, int numLEDs, double brightness) {
        this(pi4j, numLEDs, brightness, DEFAULT_SPI_CHANNEL);
    }

    /**
     * Creates a new simpleLed component with a custom BCM pin.
     *
     * @param pi4j       Pi4J context
     * @param numLEDs    How many LEDs are on this Strand
     * @param brightness How bright the leds can be at max, range 0 - 1
     * @param channel    which channel to use
     */
    public LedStrip(Context pi4j, int numLEDs, double brightness, int channel) {
        if (numLEDs < 1 || brightness < 0 || brightness > 1 || channel < 0 || channel > 1) {
            throw new IllegalArgumentException("Illegal Constructor");
        }
        logDebug("initialising a ledstrip with " + numLEDs + " leds");
        this.numLEDs = numLEDs;
        this.LEDs = new int[numLEDs];
        this.brightness = brightness;
        this.context = pi4j;
        this.spi = pi4j.create(buildSpiConfig(pi4j, channel, frequency));

        // The raw bytes that get sent to the ledstrip
        // 3 Color channels per led, at 8 bytes each, with 2 reset bytes
        pixelRaw = new byte[(3 * numLEDs * 8) + 2];

        // 1.25us per bit (1250ns)
        renderWaitTime = numLEDs * 3 * 8 * 1250 + LED_RESET_WAIT_TIME;
    }

    /**
     * Builds a new SPI instance for the LED matrix
     *
     * @param pi4j Pi4J context
     * @return SPI instance
     */
    private SpiConfig buildSpiConfig(Context pi4j, int channel, int frequency) {
        return Spi.newConfigBuilder(pi4j)
                .id("SPI" + 1)
                .name("LED Matrix")
                .address(channel)
                .mode(SpiMode.MODE_0)
                .baud(8 * frequency) //bitbanging from Bit to SPI-Byte
                .build();
    }

    /**
     * @return the pi4j context
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Setting all LEDS off and closing the strip
     */
    @Override
    public void close() {
        logInfo("Turning all leds off before close");
        allOff();
    }

    /**
     * function to get the amount of the leds on the strip
     *
     * @return int with the amount of pixels
     */
    public int getNumPixels() {
        return numLEDs;
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

    /**
     * Pixels are sent as follows: - The first transmitted pixel is the pixel
     * closest to the transmitter. - The most significant bit is always sent first.
     * <p>
     * g7,g6,g5,g4,g3,g2,g1,g0,r7,r6,r5,r4,r3,r2,r1,r0,b7,b6,b5,b4,b3,b2,b1,b0
     * \_____________________________________________________________________/ |
     * _________________... | / __________________... | / / ___________________... |
     * / / / GRB,GRB,GRB,GRB,...
     */
    @Override
    public void render() {
        //beginning at 1, because the first byte is a reset
        int counter = 1;
        for (int i = 0; i < numLEDs; i++) {

            //Scaling the color to the max brightness
            LEDs[i] = PixelColor.setRedComponent(LEDs[i], (int) (PixelColor.getRedComponent(LEDs[i]) * brightness));
            LEDs[i] = PixelColor.setGreenComponent(LEDs[i], (int) (PixelColor.getGreenComponent(LEDs[i]) * brightness));
            LEDs[i] = PixelColor.setBlueComponent(LEDs[i], (int) (PixelColor.getBlueComponent(LEDs[i]) * brightness));

            // Calculating GRB from RGB
            for (int j = 15; j >= 8; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
            for (int j = 23; j >= 16; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
            for (int j = 7; j >= 0; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
        }

        // While bitbanging, the first and last byte have to be a reset
        pixelRaw[0] = Bit_Reset;
        pixelRaw[pixelRaw.length - 1] = Bit_Reset;

        // waiting since last render time
        if (lastRenderTime != 0) {
            int diff = (int) (System.nanoTime() - lastRenderTime);
            if (renderWaitTime - diff > 0) {
                int millis = (renderWaitTime - diff) / 1_000_000;
                int nanos = (renderWaitTime - diff) % 1_000_000;
                sleep(millis, nanos);
            }
        }

        //writing on the PIN
        spi.write(pixelRaw);

        logDebug("finished rendering");
        lastRenderTime = System.nanoTime();
    }

    /**
     * setting all LEDs off
     */
    @Override
    public void allOff() {
        Arrays.fill(LEDs, 0);
        render();
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds. An {@link InterruptedException} will be caught and ignored while setting the
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

    /**
     * Helper Class specific for only LEDStrips and matrices
     * can calculate different colors, and gets the individual color channels
     */
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

        /**
         * Input a value 0 to 255 to get a Color value.
         * The Colors are a transition r - g - b - back to r.
         *
         * @param wheelPos Position on the Color wheel (range 0..255).
         * @return 24-bit RGB Color value
         */
        public static int wheel(int wheelPos) {
            int max = Color_COMPONENT_MAX;
            int one_third = Color_COMPONENT_MAX / 3;
            int two_thirds = Color_COMPONENT_MAX * 2 / 3;

            int wheel_pos = max - wheelPos;
            if (wheel_pos < one_third) {
                return createColorRGB(max - wheel_pos * 3, 0, wheel_pos * 3);
            }
            if (wheel_pos < two_thirds) {
                wheel_pos -= one_third;
                return createColorRGB(0, wheel_pos * 3, max - wheel_pos * 3);
            }
            wheel_pos -= two_thirds;
            return createColorRGB(wheel_pos * 3, max - wheel_pos * 3, 0);
        }

        /**
         * Create a Color from relative RGB values
         *
         * @param red   Red %, {@code 0 to 1}
         * @param green Green %, {@code 0 to 1}
         * @param blue  Blue %, {@code 0 to 1}
         * @return RGB Color integer value
         */
        public static int createColorRGB(float red, float green, float blue) {
            return createColorRGB(Math.round(Color_COMPONENT_MAX * red),
                    Math.round(Color_COMPONENT_MAX * green), Math.round(Color_COMPONENT_MAX * blue));
        }

        /**
         * Create a Color from int RGB values
         *
         * @param red   Red component {@code 0 to 255}
         * @param green Green component {@code 0 to 255}
         * @param blue  Blue component {@code 0 to 255}
         * @return RGB Color integer value
         */
        public static int createColorRGB(int red, int green, int blue) {
            validateColorComponent("Red", red);
            validateColorComponent("Green", green);
            validateColorComponent("Blue", blue);
            return red << 16 | green << 8 | blue;
        }

        /**
         * Creates a Color based on the specified values in the HSL Color model.
         *
         * @param hue        The hue, in degrees, {@code 0.0 to 360.0}
         * @param saturation The saturation %, {@code 0.0 to 1.0}
         * @param luminance  The luminance %, {@code 0.0 to 1.0}
         * @return RGB Color integer value
         * @throws IllegalArgumentException if {@code hue}, {@code saturation}, {@code brightness} are out of range
         */
        public static int createColorHSL(float hue, float saturation, float luminance) {
            // Hue Saturation Luminance - see https://tips4java.wordpress.com/2009/07/05/hsl-color/

            if (saturation < 0.0f) {
                saturation = 0;
            }
            if (saturation > 1.0f) {
                saturation = 1;
            }

            if (luminance < 0.0f || luminance > 1.0f) {
                String message = "Color parameter outside of expected range - Luminance";
                throw new IllegalArgumentException(message);
            }

            // Formula needs all values between 0 - 1.
            hue = hue % 360.0f;
            hue /= 360f;

            float q = 0;

            if (luminance < 0.5)
                q = luminance * (1 + saturation);
            else
                q = (luminance + saturation) - (saturation * luminance);

            float p = 2 * luminance - q;

            float r = Math.max(0, HueToRGB(p, q, hue + (1.0f / 3.0f)));
            float g = Math.max(0, HueToRGB(p, q, hue));
            float b = Math.max(0, HueToRGB(p, q, hue - (1.0f / 3.0f)));

            r = Math.min(r, 1.0f);
            g = Math.min(g, 1.0f);
            b = Math.min(b, 1.0f);

            return createColorRGB(r, g, b);
        }

        /**
         * Calculating the RGB Value of a HUE color
         *
         * @param p
         * @param q
         * @param h
         * @return the corresponding RGB color
         */
        private static float HueToRGB(float p, float q, float h) {
            if (h < 0)
                h += 1;

            if (h > 1)
                h -= 1;

            if (6 * h < 1) {
                return p + ((q - p) * 6 * h);
            }

            if (2 * h < 1) {
                return q;
            }

            if (3 * h < 2) {
                return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
            }

            return p;
        }

        /**
         * validate if the color channel is in a valid range
         *
         * @param color the color which is to check
         * @param value the color channel value
         */
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
