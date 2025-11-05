package com.example.commerce_back_office.exception.auth;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 토큰이 유실되었을때 던지는 예외
 */
@Getter
public class TokenMissingException extends RuntimeException {

    private final ErrorCode errorCode;

    public TokenMissingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
