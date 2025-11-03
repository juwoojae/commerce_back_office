package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.UserDetailResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class joinResponseDto {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private LocalDateTime createdDate;

    public joinResponseDto(Long id, String email, String name, UserRole role, LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.email = email;
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static joinResponseDto from(User user) {
        return new joinResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedDate()
        );
    }
}
