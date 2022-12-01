package com.pi4j.components.interfaces;

public interface LEDStripInterface {
    void close();

    int getPixelColor(int pixel);

    void setPixelColor(int pixel, int color);

    void setStripColor(int color);

    void render();

    void allOff();

    void sleep(long millis, int nanos);

    double getBrightness();

    void setBrightness(double brightness);
}
