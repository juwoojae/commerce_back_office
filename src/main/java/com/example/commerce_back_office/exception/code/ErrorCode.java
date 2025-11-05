package com.example.commerce_back_office.exception.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode {
    ;

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
