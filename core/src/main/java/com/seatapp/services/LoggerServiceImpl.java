package com.seatapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerServiceImpl implements LoggerService {
    /**
     * Represents the logger.
     */
    private final Logger logger;

    /**
     * Create a logger service.
     *
     * @param loggerCless the class in which the logger will be used
     */
    public LoggerServiceImpl(final Class<?> loggerCless) {
        logger = LoggerFactory.getLogger(loggerCless);
    }

    /**
     * This logs the exception with a message.
     *
     * @param message the message for the logger
     */
    @Override
    public void debug(final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
