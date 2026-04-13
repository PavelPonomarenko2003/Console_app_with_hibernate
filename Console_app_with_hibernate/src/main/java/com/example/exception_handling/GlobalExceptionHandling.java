package com.example.exception_handling;

import com.example.dto.ErrorResponseDTO;
import com.example.exception.BasicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // Важный импорт!
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandling {

    // 1. Control mine exceptions
    @ExceptionHandler(BasicException.class)
    public ResponseEntity<ErrorResponseDTO> handleBaseExceptions(BasicException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.getErrorCode()
        );
        return new ResponseEntity<>(error, exception.getStatus());
    }

    // I've made it to get status 400...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Собираем все ошибки валидации в одну строку
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                "Validation failed: " + errorMessage,
                "BAD_REQUEST"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handling all other exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleUnknownExceptions(Exception exception) {
        // Для отладки можно добавить вывод в консоль: exception.printStackTrace();
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                "Internal Server Error: " + exception.getMessage(),
                "SERVER_ERROR"
        );
    }
}
