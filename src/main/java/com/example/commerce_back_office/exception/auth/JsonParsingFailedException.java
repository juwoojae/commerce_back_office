package com.example.commerce_back_office.exception.auth;

import com.example.commerce_back_office.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 로그인 요청에서 로그인 입력 양식이 타당하지 않아서
 * Json 으로 파싱에 실패한 경우
 */
@Getter
public class JsonParsingFailedException extends RuntimeException {

    private final ErrorCode errorCode;

    public JsonParsingFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
