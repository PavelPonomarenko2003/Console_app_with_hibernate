package com.example.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BasicException {
    public UserAlreadyExistsException(String email) {
        super(
                "User with email " + email + " already exists",
                "USER_ALREADY_EXISTS",
                HttpStatus.CONFLICT // status 409
        );
    }
}
