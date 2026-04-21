package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Getter
@AllArgsConstructor
public class UserCreatingDTO {

    @Size(min = 2, max = 50)
    @NotNull(message = "name is required field")
    private String name;

    @NotNull(message = "age is required field")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@mail\\.ru$",
            message = "Email has to be with @mail.ru"
    )
    private String email;

    @NotNull
    @Positive(message = "Age has to be positive")
    private Integer age;

    public UserCreatingDTO() {
    }

}
