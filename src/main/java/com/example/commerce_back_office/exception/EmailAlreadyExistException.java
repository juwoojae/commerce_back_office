package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 도메인 예외
 * 하나의 사용자가 중복된 이메일로 회원가입을 시도하는 경우
 */
@Getter
public class EmailAlreadyExistException extends RuntimeException {

    private ErrorCode errorCode;

    public EmailAlreadyExistException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
