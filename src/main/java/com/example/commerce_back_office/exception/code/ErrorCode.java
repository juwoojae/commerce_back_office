package com.example.commerce_back_office.exception.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode {

    //인증, 인가 예외

    TOKEN_MISSING(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST, "요청 데이터를 파싱할 수 없습니다."),

    //커스텀 예외
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
