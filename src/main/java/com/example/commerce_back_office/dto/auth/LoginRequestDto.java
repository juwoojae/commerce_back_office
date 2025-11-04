package com.example.commerce_back_office.dto.auth;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}