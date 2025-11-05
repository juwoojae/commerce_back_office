package com.example.commerce_back_office.exception;

/**
 * 로그인 요청에서 로그인 입력 양식이 타당하지 않아서
 * Json 으로 파싱에 실패한 경우
 */
public class JsonParsingFailedException extends RuntimeException {
    public JsonParsingFailedException(String message) {
        super(message);
    }
}
