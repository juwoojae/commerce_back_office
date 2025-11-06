package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 권한이 부족해서 클라이언트 요청을 수행하지 못할때 던지는 예외
 */
@Getter
public class UnauthorizedActionException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnauthorizedActionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
