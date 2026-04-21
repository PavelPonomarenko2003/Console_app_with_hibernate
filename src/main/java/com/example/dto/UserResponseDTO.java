package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Getter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Integer age;

    public UserResponseDTO() {
    }

}
