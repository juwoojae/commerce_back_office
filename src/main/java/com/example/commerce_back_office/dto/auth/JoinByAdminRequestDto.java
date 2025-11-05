package com.example.commerce_back_office.dto.auth;

import com.example.commerce_back_office.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;
/**
 * 관리자에 의한 회원가입시 요청 DTO
 * 나중에 확장성을 생각해서 JoinRequestDto 와 별도로 만듬
 */
@Getter
public class JoinByAdminRequestDto {

    @Email(message = "이메일 형식으로 입력하세요")
    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호의 길이는 최소 8자 입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "영문 + 숫자 + 특수 문자가 포함")
    private String password;
    @NotNull
    private String name;
    private UserRole role;

}
