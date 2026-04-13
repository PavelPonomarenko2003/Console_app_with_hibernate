package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a user's profile details")
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {

    @Schema(description = "Unique database identifier", example = "1")
    private Long id;

    @Schema(description = "User's full name", example = "Alice")
    @Size(min = 2, max = 50)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "User's verified email address", example = "Alice@mail.ru")
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@mail\\.ru$",
            message = "Email must end with @mail.ru"
    )
    private String email;

    @Schema(description = "User's current age", example = "25")
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be positive")
    private Integer age;
}
