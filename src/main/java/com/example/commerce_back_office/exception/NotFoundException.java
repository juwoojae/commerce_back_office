package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 해당 엔티티가 db 에서 발견할수 없을때 던지는 예외
 */
@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
