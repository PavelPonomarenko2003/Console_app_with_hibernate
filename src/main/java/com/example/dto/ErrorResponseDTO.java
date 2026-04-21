package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private String message;
    private String errorCode;

    public ErrorResponseDTO(String message, String errorCode) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errorCode = errorCode;
    }



}
