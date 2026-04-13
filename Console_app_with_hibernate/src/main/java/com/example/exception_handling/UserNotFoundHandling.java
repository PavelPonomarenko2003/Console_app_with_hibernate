package com.example.exception_handling;

import com.example.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dto.ErrorResponseDTO;

import java.time.LocalDateTime;

@RestControllerAdvice // AOP working with proxying
public class UserNotFoundHandling {

    @ExceptionHandler(UserNotFoundException.class) // sending to user understandable message if we get some troubles
    @ResponseStatus(HttpStatus.NOT_FOUND) // status expected, in that situation status is sable NOT_FOUND
    public ErrorResponseDTO handleExceptionUserNotFound(UserNotFoundException exception){
        return new ErrorResponseDTO(LocalDateTime.now(), exception.getMessage(), "USER_NOT_FOUND");
    }

}
