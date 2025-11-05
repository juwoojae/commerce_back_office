package com.example.commerce_back_office.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원가입시 클라이언트의 요청 데이터를 담는 DTO
 *
 */
@Getter
public class JoinRequestDto {

    @Email(message = "이메일 형식으로 입력하세요")
    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호의 길이는 최소 8자 입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "영문 + 숫자 + 특수 문자가 포함")
    private String password;
    @NotNull
    private String name;

}