package com.github.gypsyjr777.discordmanager.exception;

public class NullMemberException extends AppException {
    private static final String MESSAGE = "Member did not find: ";
    public NullMemberException(String message) {
        super(MESSAGE + message);
    }
}
