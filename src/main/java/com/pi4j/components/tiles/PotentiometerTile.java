package com.pi4j.components.tiles;

import com.pi4j.components.components.Ads1115;
import com.pi4j.components.interfaces.PotentiometerInterface;
import com.pi4j.components.tiles.Skins.PotentiometerTileSkin;

import java.util.function.Consumer;

public class PotentiometerTile extends Pi4JTile implements PotentiometerInterface {

    private Consumer<Double> xOnMove;

    private double xStart;
    private double currentX;

    private double normX;

    private double lineLength;


    PotentiometerTileSkin potentiometerSkin = new PotentiometerTileSkin(this);

    public PotentiometerTile(Ads1115 ads1115, int channel, double maxVoltage) {
        setText("Channel: " + channel + ", MaxVoltage: " + maxVoltage);
        constructorValues();
    }
    public PotentiometerTile(Ads1115 ads1115) {
        constructorValues();
    }


    public double getNormX() {
        return normX;
    }

    public void setNormX(double normX) {
        this.normX = normX;
    }



    public void updatePos(){
        setDescription(String.format("%.2f", getNormX())+" %");
    }

    @Override
    public double singleShotGetNormalizedValue() {
        return normX;
    }

    @Override
    public void setConsumerFastRead(Consumer<Double> method) {

    }

    @Override
    public void setConsumerSlowReadChan(Consumer<Double> method) {
        xOnMove = value -> {

            value = currentX;
            //scale axis from 0 to 100
            value = 100 / lineLength * value;

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

    public void constructorValues(){
        setNormX(0.0);
        setTitle("Potentiometer");
        setDescription(String.format("%.2f", getNormX()) + " %");
        setSkin(potentiometerSkin);

        //TODO: Wert finden, welches nicht absolut ist
        lineLength = 200;


        potentiometerSkin.getKnob().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - potentiometerSkin.getKnob().getTranslateX();
        });

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
