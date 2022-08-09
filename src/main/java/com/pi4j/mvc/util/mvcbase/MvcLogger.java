package com.pi4j.mvc.util.mvcbase;

import java.util.logging.Logger;

public final class MvcLogger {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void logInfo(String msg) {
        logger.info(() -> msg);
    }

    public void logError(String msg) {
        logger.severe(() -> msg);
    }

    public void logConfig(String msg) {
        logger.config(() -> msg);
    }

    public void logDebug(String msg) {
        logger.fine(() -> msg);
    }
}
