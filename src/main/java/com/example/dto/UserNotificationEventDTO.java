package com.example.dto;

import com.example.entity.Action;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing a user notification event sent to the message broker")
public class UserNotificationEventDTO implements Serializable {

    @Schema(description = "Unique identifier of the event", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID eventId;

    @Schema(description = "Name of the user involved in the event", example = "Alice")
    @Size(min = 2, max = 50)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "User's email address", example = "Alice@mail.ru")
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@mail\\.ru$",
            message = "Email must end with @mail.ru"
    )
    private String email;

    @Schema(description = "Type of action performed", example = "USER_CREATED")
    private Action actionType;

    @Schema(description = "Timestamp when the event occurred", example = "2023-10-27:15:30")
    private LocalDateTime timestamp;
}

