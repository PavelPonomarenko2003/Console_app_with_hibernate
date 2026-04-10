package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information required to create a new user")
public class UserCreatingDTO {

    @Schema(description = "User's full name", example = "Alice")
    @Size(min = 2, max = 50)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(
            description = "User's email address (must be @mail.ru)",
            example = "Alcie@mail.ru"
    )
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@mail\\.ru$",
            message = "Email must end with @mail.ru"
    )
    private String email;

    @Schema(description = "User's age", example = "25")
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be positive")
    private Integer age;
}
