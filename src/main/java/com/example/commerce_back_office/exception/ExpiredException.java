package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 토큰이 만료되었을때 던지는 예외
 */
@Getter
public class ExpiredException extends RuntimeException {

    private final ErrorCode errorCode;

    public ExpiredException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
