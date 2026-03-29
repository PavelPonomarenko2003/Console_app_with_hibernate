package com.example.exception_handling;

import com.example.dto.ErrorResponseDTO;
import com.example.exception.BasicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    // control checked exceptions
    @ExceptionHandler(BasicException.class)
    public ResponseEntity<ErrorResponseDTO> handleBaseExceptions(BasicException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO(exception.getMessage(), exception.getErrorCode());
        // we get status from certain exception...more flexible
        return new ResponseEntity<>(error, exception.getStatus());
    }

    // handling all other exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // constant status, we don't need others
    public ErrorResponseDTO handleUnknownExceptions(Exception exception) {
        return new ErrorResponseDTO("Internal Server Error", "SERVER_ERROR");
    }

}
