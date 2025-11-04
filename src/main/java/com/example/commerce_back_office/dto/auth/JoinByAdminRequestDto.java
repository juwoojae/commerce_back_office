package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JoinByAdminRequestDto {
    private String email;
    private String password;
    private String name;
    private UserRole role;
    private LocalDateTime createdDate;

}
