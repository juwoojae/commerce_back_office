package com.example.commerce_back_office.dto.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 전체 예외 처리 필터에서
 * 예외가 발생할경우 클라이언트에 보내는 응답 메서지 DTO
 */
@Getter
public class AuthErrorResponseDto {
    private HttpStatus status;
    private String  message;
    private String path;

    public AuthErrorResponseDto(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
