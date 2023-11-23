package com.github.gypsyjr777.discordmanager.exception;

public class NullChannelException extends AppException {
    private static final String MESSAGE = "Channel did not find: ";
    public NullChannelException(String message) {
        super(MESSAGE + message);
    }
}
