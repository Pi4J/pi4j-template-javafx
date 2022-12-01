package com.pi4j.components.interfaces;

public interface LedMatrixInterface {
    void close();

    void render();

    void allOff();

    void setPixelColor(int strip, int pixel, int color);

    void setMatrixPixelColor(int pixelNumber, int color);

    void setStripColor(int strip, int color);

    void setMatrixColor(int color);


    void setBrightness(double brightness);
}
