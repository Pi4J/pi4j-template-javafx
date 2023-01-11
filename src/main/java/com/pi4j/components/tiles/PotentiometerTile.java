package com.pi4j.components.tiles;

import com.pi4j.components.components.helpers.PIN;
import com.pi4j.components.interfaces.PotentiometerInterface;
import com.pi4j.components.tiles.Skins.PotentiometerTileSkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PotentiometerTile extends Pi4JTile implements PotentiometerInterface {

    private Consumer<Double> xOnMove;

    private double xStart;
    private double currentX;

    private double normX;

    private double lineLength;


    PotentiometerTileSkin potentiometerSkin = new PotentiometerTileSkin(this);
    public PotentiometerTile(PIN pin, String ads_address) {
        setNormX(0.0);
        setTitle("Potentiometer");
        setText("Pin " + pin.getPin() + ", ADS: " + ads_address);
        setDescription("X: (" + String.format("%.2f", getNormX()) + ")");
        setSkin(potentiometerSkin);

        lineLength = 200;


        potentiometerSkin.getButton().setOnMousePressed(mouseEvent -> {
            xStart = mouseEvent.getSceneX() - potentiometerSkin.getButton().getTranslateX();
        });

        potentiometerSkin.getButton().setOnMouseDragged(mouseEvent -> {

            if (mouseEvent.getSceneX() - xStart < lineLength
                && mouseEvent.getSceneX() - xStart > 0) {
                potentiometerSkin.getButton().setTranslateX(mouseEvent.getSceneX() - xStart);
                currentX = mouseEvent.getSceneX() - xStart;
                xOnMove.accept(currentX);
            }

        });
    }


    public double getNormX() {
        return normX;
    }

    public void setNormX(double normX) {
        this.normX = normX;
    }



    public void updatePos(){
        setDescription("X: ("+String.format("%.2f", getNormX())+")");
    }

    @Override
    public double singleShotGetNormalizedValue() {
        return 0;
    }

    @Override
    public void setConsumerFastRead(Consumer<Double> method) {

    }

    @Override
    public void setConsumerSlowReadChan(Consumer<Double> method) {
        xOnMove = value -> {

            value = currentX;
            //scale axis from 0 to 1
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
}
