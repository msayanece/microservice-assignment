package com.sayan.userauth.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserNotValidException extends Exception {
    public UserNotValidException(String message) {
        super(message);
    }

    public UserNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
