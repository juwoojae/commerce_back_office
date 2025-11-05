package com.example.commerce_back_office.exception.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode {

    //인증, 인가 예외

    TOKEN_MISSING(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST, "요청 데이터를 파싱할 수 없습니다."),


    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문이 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),

    //도메인 예외
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALIDATE_QUANTITY(HttpStatus.BAD_REQUEST, "재고 수량은 0 이하로 설정할 수 없습니다."),

    //@Validation 유효성 검사 실패시 처리
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다.");

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
