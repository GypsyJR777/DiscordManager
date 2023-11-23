package com.github.gypsyjr777.discordmanager.exception;

public class NullUserException extends AppException {
    private static final String MESSAGE = "User did not find: ";
    public NullUserException(String message) {
        super(MESSAGE + message);
    }
}
