package com.example.exception;

import org.springframework.http.HttpStatus;

public class IncorrectDataException extends BasicException {
    public IncorrectDataException(String message) {
        super(message, "INCORRECT_DATA", HttpStatus.BAD_REQUEST); // status 400
    }
}