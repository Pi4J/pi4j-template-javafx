package com.pi4j.catalog.components.button;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pi4j.io.gpio.digital.DigitalState;

import com.pi4j.catalog.components.base.Component;


public class ButtonEventHelper extends Component {
    private final Button button;

    /**
     * Runnable Code when the button is depressed
     */
    private volatile Runnable onUp;

    /**
     * Runnable Code when the button is pressed
     */
    private volatile Runnable onDown;

    /**
     * Handler while the button is pressed
     */
    private volatile Runnable whileDown;

    private final Object lock = new Object();

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private volatile Duration whilePressedDelay;
    private volatile Future<?> whileDownFuture = null;

    ButtonEventHelper(Button button){
        this.button = button;
    }

    void onStateChange(DigitalState ds){
        DigitalState state = (button.hasPullUpResistor()) ?
                ds :
                // if the button is wired using a pull-down resistor, the state must be inverted
                (ds == DigitalState.HIGH) ? DigitalState.LOW : DigitalState.HIGH;

        switch (state) {
            case LOW -> {
                if (onDown != null) {
                    logDebug("onDown triggered");
                    onDown.run();
                }
                if (whileDown != null) {
                    synchronized (lock) {
                        if (whileDownFuture == null && !executor.isShutdown()) {
                            whileDownFuture = executor.submit(() -> runWhileDown());
                        }
                    }
                }
            }
            case HIGH -> {
                if (onUp != null) {
                    logDebug("onUp triggered");
                    onUp.run();
                }
            }
            case UNKNOWN -> logError("Button is in State UNKNOWN");
        }
    }

    public void onDown(Runnable task) {
        onDown = task;
    }

    public void onUp(Runnable task) {
        onUp = task;
    }

    public void whilePressed(Runnable task, Duration delay) {
        Objects.requireNonNull(delay);

        synchronized (lock) {
            whileDown = task;
            whilePressedDelay = delay;
            cancelWhileDown();
        }
    }

    private void runWhileDown() {
        try {
            while (button.isPressed() && !Thread.currentThread().isInterrupted()) {
                delay(whilePressedDelay);
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                Runnable task = whileDown;
                if (button.isPressed() && task != null) {
                    logDebug("whileDown triggered");
                    task.run();
                }
            }
        } finally {
            synchronized (lock) {
                whileDownFuture = null;
            }
        }
    }

    private void cancelWhileDown() {
        if (whileDownFuture != null) {
            whileDownFuture.cancel(true);
            whileDownFuture = null;
        }
    }

    public void shutdown() {
        synchronized (lock) {
            onDown = null;
            onUp = null;
            whileDown = null;
            cancelWhileDown();
        }
        executor.shutdown();
        super.shutdown();
    }

    public boolean isInInitialState(){
        synchronized (lock) {
            return onDown == null && onUp == null && whileDown == null && whileDownFuture == null;
        }
    }
}
