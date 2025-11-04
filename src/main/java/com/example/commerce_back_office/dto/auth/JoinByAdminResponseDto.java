package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 관리자에 의한 회원가입시 응답 데이터를 담는 DTO
 * 나중에 확장성을 생각해서 JoinResponseDto 와 별도로 만듬
 */
@Getter
public class JoinByAdminResponseDto {
    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private LocalDateTime createdDate;


    public JoinByAdminResponseDto(Long id, String email, String name, UserRole role, LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.email = email;
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static JoinByAdminResponseDto from(User user) {
        return new JoinByAdminResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedDate()
        );
    }
}
