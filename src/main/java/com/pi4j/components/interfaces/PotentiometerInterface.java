package com.pi4j.components.interfaces;

import java.util.function.Consumer;

public interface PotentiometerInterface {
    double singleShotGetNormalizedValue();

    void setConsumerFastRead(Consumer<Double> method);

    void setConsumerSlowReadChan(Consumer<Double> method);

    void startSlowContinuousReading(double threshold, int readFrequency);

    void stopSlowContinuousReading();

    void startFastContinuousReading(double threshold, int readFrequency);

    void stopFastContinuousReading();
}
