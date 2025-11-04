package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;
/**
 * 관리자에 의한 회원가입시 요청 DTO
 * 나중에 확장성을 생각해서 JoinRequestDto 와 별도로 만듬
 */
@Getter
public class JoinByAdminRequestDto {
    private String email;
    private String password;
    private String name;
    private UserRole role;

}
