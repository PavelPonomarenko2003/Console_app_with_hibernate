package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    private Integer age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserEntity(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public UserEntity(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public UserEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserEntity user = (UserEntity) o;

        return Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, age);
    }

}
