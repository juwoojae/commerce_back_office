package com.example.commerce_back_office.dto.auth;

import lombok.Getter;

@Getter
public class RefreshResponseDto {
    private final String AccessToken;
    private final String RefreshToken;

    public RefreshResponseDto(String accessToken, String refreshToken) {
        AccessToken = accessToken;
        RefreshToken = refreshToken;
    }
}
