package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
/**
 * 회원가입시 클라이언트의 응답 데이터를 담는 DTO
 * test
 */
@Getter
public class JoinResponseDto {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private LocalDateTime createdDate;

    public JoinResponseDto(Long id, String email, String name, UserRole role, LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.email = email;
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static JoinResponseDto from(User user) {
        return new JoinResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedDate()
        );
    }
}
