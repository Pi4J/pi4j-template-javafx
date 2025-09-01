package com.pi4j.mvc.util.mvcbase;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple logger utility class for managing and controlling log output within an application.
 * <p>
 * This class provides methods for logging messages at various levels such as INFO, ERROR, CONFIG,
 * and DEBUG. It also supports logging exceptions with an associated message.
 * <p>
 * The logger is configured to use a custom format and operates without parent handlers, ensuring
 * that logs are formatted consistently and printed only through this logger.
 * <p>
 * Designed as a singleton, this class ensures centralized logging by exposing a single shared
 * instance via the LOGGER field.
 */
public final class MvcLogger {
    private final Logger logger = Logger.getLogger("Pi4J Template Project");

    public static final MvcLogger LOGGER = new MvcLogger();

    private MvcLogger(){
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

    public void logInfo(String msg, Object... args) {
        logger.info(() -> String.format(msg, args));
    }

    public void logError(String msg, Object... args) {
        logger.severe(() -> String.format(msg, args));
    }

    public void logConfig(String msg, Object... args) {
        logger.config(() -> String.format(msg, args));
    }

    public void logDebug(String msg, Object... args) {
        logger.fine(() -> String.format(msg, args));
    }

    public void logException(String msg, Throwable exception){
        logger.log(Level.SEVERE, msg, exception);
    }
}
