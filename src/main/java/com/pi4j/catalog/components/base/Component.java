package com.pi4j.catalog.components.base;

import java.time.Duration;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Component {
    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger("Pi4J Components");

    static {
        Level appropriateLevel = Level.INFO;
        //Level appropriateLevel = Level.FINE; //use if 'debug'

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tl:%1$tM:%1$tS %1$Tp]%n");

        logger.setLevel(appropriateLevel);
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(appropriateLevel);
        logger.addHandler(handler);
    }

    protected Component(){
    }

    /**
     * Override this method to clean up all used resources
     */
    public void reset(){
        //nothing to do by default
    }

    protected void logInfo(String msg, Object... args) {
        logger.info(() -> String.format(msg, args));
    }

    protected void logError(String msg, Object... args) {
        logger.severe(() -> String.format(msg, args));
    }

    protected void logDebug(String msg, Object... args) {
        logger.fine(() -> String.format(msg, args));
    }

    protected void logException(String msg, Throwable exception){
        logger.log(Level.SEVERE, msg, exception);
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be caught and ignored while setting the interrupt flag again.
     *
     * @param duration Time to sleep
     */
    protected void delay(Duration duration) {
        try {
            long nanos = duration.toNanos();
            long millis = nanos / 1_000_000;
            int remainingNanos = (int) (nanos % 1_000_000);
            Thread.currentThread().sleep(millis, remainingNanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected <T> T asMock(Class<T> type, Object instance) {
        return type.cast(instance);
    }
}
