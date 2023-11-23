package com.github.gypsyjr777.discordmanager.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppException extends RuntimeException {
    private Logger log = LogManager.getLogger(AppException.class);


    public AppException(String message) {
        log.error(message);
    }
}
