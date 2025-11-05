package com.example.commerce_back_office.exception.code;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

/**
 * 성공 코드에 대한
 * message , 응답 코드를 Enum 으로 관리
 */
public enum SuccessCode implements BaseCode {

    //product
    GET_PRODUCTS(HttpStatus.OK, "상품 리스트 조회 + 검색 성공"),
    GET_PRODUCT(HttpStatus.OK, "상품 상세조회 성공"),
    CREATE_PRODUCT(HttpStatus.CREATED, "상품 등록 성공"),
    UPDATE_PRODUCT(HttpStatus.OK, "상품 정보 수정 성공"),

    //User
    GET_USERS(HttpStatus.OK, "유저 리스트 조회 성공"),
    GET_USER(HttpStatus.OK, "유저 상세조회 성공"),
    GET_USER_BY_KEYWORD(HttpStatus.OK, "유저 키워드 기반 검색 성공"),
    UPDATE_USER(HttpStatus.OK, "유저 정보 수정 성공"),

    //Auth
    JOIN_PROCESS(HttpStatus.CREATED, "유저 회원가입 성공"),
    JOIN_JOIN_PROCESS_BY_ADMIN(HttpStatus.CREATED, "관리자에 의한 회원가입 성공"),
    REFRESH_PROCESS(HttpStatus.OK, "토큰 재발행 성공");



    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
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
