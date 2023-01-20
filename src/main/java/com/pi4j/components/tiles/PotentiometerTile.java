package com.pi4j.components.tiles;

import com.pi4j.components.components.Ads1115;
import com.pi4j.components.interfaces.PotentiometerInterface;
import com.pi4j.components.tiles.Skins.PotentiometerSkin;

import java.util.function.Consumer;

public class PotentiometerTile extends Pi4JTile implements PotentiometerInterface {

    private double xStart;
    private double currentX;

    private double normX;

    private double lineLength;

    private Consumer<Double> xOnMove;

    PotentiometerSkin potentiometerSkin = new PotentiometerSkin(this);

    public PotentiometerTile(Ads1115 ads1115, int channel, double maxVoltage) {
        setText("Channel: " + channel + ", MaxVoltage: " + maxVoltage);
        constructorValues();
    }

    public PotentiometerTile(Ads1115 ads1115) {
        constructorValues();
    }

    /**
     * Returns normalized value from 0 to 1
     *
     * @return normalized value
     */
    @Override
    public double singleShotGetNormalizedValue() {
        return normX;
    }

    @Override
    public void setConsumerFastRead(Consumer<Double> method) {

    }

    /**
     * Sets or disables the handler for the onValueChange event.
     * This event gets triggered whenever the value changes.
     * Only a single event handler can be registered at once.
     *
     * @param method Event handler to call or null to disable
     */
    @Override
    public void setConsumerSlowReadChan(Consumer<Double> method) {
        xOnMove = value -> {

            value = currentX;
            //scale axis from 0 to 1
            value = 1 / lineLength * value;

            setNormX(value);
            updatePos();
            method.accept(value);

        };
    }

    @Override
    public void startSlowContinuousReading(double threshold, int readFrequency) {

    }

    @Override
    public void stopSlowContinuousReading() {

    }

    @Override
    public void startFastContinuousReading(double threshold, int readFrequency) {

    }

    @Override
    public void stopFastContinuousReading() {

    }

    public double getNormX() {
        return normX;
    }

    public void setNormX(double normX) {
        this.normX = normX;
    }


    // Displays current percent on the GUI
    public void updatePos(){
        setDescription(String.format("%.2f", getNormX()));
    }

    // Helper function. Add same content in all constructors
    public void constructorValues(){
        setNormX(0.0);
        setTitle("Potentiometer");
        setDescription(String.format("%.2f", getNormX()));
        setSkin(potentiometerSkin);

        //TODO: Wert finden, welches nicht absolut ist
        lineLength = 200;


        //Register current position
        potentiometerSkin.getKnob().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - potentiometerSkin.getKnob().getTranslateX();
        });

        // Allow moving knob on the line
        potentiometerSkin.getKnob().setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getSceneX() - xStart < lineLength
                && mouseEvent.getSceneX() - xStart > 0) {
                potentiometerSkin.getKnob().setTranslateX(mouseEvent.getSceneX() - xStart);
                currentX = mouseEvent.getSceneX() - xStart;
                xOnMove.accept(currentX);
            }

        });

    }
}
