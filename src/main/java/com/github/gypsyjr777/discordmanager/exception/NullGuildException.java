package com.github.gypsyjr777.discordmanager.exception;

public class NullGuildException extends AppException {
    private static final String MESSAGE = "Guild did not find: ";
    public NullGuildException(String message) {
        super(MESSAGE + message);
    }
}
