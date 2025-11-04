package com.example.commerce_back_office.dto;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final UserRole role;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public UserResponseDto(Long id, String email, String name, UserRole role, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.created_at = createdDate;
        this.updated_at = lastModifiedDate;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedDate(),
                user.getLastModifiedDate()
        );
    }
}
