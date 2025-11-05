package com.example.commerce_back_office.exception.auth;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 토큰이 시그니처 검증에 실패했을때 던지는 예외
 */
@Getter
public class InvalidTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
