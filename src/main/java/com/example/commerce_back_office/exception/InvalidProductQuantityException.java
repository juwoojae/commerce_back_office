package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * Product 의 수량을 0 이하로 수정하려고 할때 던져지는 예외
 */
@Getter
public class InvalidProductQuantityException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidProductQuantityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
