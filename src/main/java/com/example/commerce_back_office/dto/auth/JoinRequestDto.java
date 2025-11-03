package com.example.commerce_back_office.dto.auth;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원가입시 클라이언트의 요청 데이터를 담는 DTO
 *
 */
@Getter
public class JoinRequestDto {

    private String email;
    private String password;
    private String name;
    private LocalDateTime createdDate;

}