package com.pi4j.mvc.util.puicomponentbase.helpers;

public class Logger {
    private final org.slf4j.Logger logger;

    public Logger(Class<?> c) {
        this.logger = org.slf4j.LoggerFactory.getLogger(c);
    }

    public void trace(String message, Object... args) {
        logger.trace(message, args);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void error(String message, Object... args) {
        logger.error(message, args);
    }
}
