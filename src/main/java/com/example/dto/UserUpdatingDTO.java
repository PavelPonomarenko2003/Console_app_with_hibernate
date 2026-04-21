package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information for updating an existing user's profile")
public class UserUpdatingDTO {

    @Schema(description = "Updated full name of the user", example = "Alice")
    @Size(min = 2, max = 50)
    @NotBlank(message = "name is required field")
    private String name;

    @Schema(
            description = "Updated email address (must end with @mail.ru)",
            example = "Alice@mail.ru"
    )
    @NotBlank(message = "Email is required field")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@mail\\.ru$",
            message = "Email has to be with @mail.ru"
    )
    private String email;

    @Schema(description = "Updated age of the user", example = "26")
    @NotNull
    @Positive(message = "Age has to be positive")
    private Integer age;
}
