package com.example.dto;

import com.example.entity.Action;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard API error response structure")
public class ErrorResponseDTO {

    @Schema(description = "Timestamp of the error", example = "2023-10-27T10:15:30")
    private LocalDateTime timestamp;

    @Schema(description = "Detailed error message", example = "User with given ID not found")
    private String message;

    @Schema(description = "Internal application error code", example = "FAILED")
    private Action errorCode;

    public ErrorResponseDTO(LocalDateTime now, String message, String errorCode) {
    }
}
