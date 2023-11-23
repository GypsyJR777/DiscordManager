package com.github.gypsyjr777.discordmanager.exception;

public class NullRoleException extends AppException {
    private static final String MESSAGE = "Role did not find: ";


    public NullRoleException(String message) {
        super(MESSAGE + message);
    }
}
