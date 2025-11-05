package com.example.commerce_back_office.exception;

/**
 *
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
