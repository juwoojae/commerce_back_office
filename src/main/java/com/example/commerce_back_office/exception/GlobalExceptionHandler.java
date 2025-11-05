package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.commerce_back_office.exception.code.ErrorCode.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.*;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. @Valid 유효성 검사 실패 시 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // 각 필드별 오류 메시지를 Map에 저장
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(BAD_REQUEST).body(CommonResponse.of(VALIDATION_ERROR, errors));
    }

    /**
     * 2. 재고가 0 이하로 입력되는 경우 예외 처리
     */
    @ExceptionHandler(InvalidProductQuantityException.class)
    public ResponseEntity<CommonResponse<Object>> InvalidProductQuantityException(
            InvalidProductQuantityException ex) {

        return ResponseEntity.status(BAD_REQUEST).body(CommonResponse.of(ex.getErrorCode()));
    }

    /**
     * 3. 존재하지 않는 상품 조회 예외 처리
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResponse<Object>> handleRuntime(NotFoundException ex) {

        return ResponseEntity.status(NOT_FOUND).body(CommonResponse.of(ex.getErrorCode()));
    }

    /**
     * 도메인 정책 위반 - 회원 등록에서 하나의 이메일을 중복등록 하는경우
     */
    @ExceptionHandler (EmailAlreadyExistException.class)
    public ResponseEntity<CommonResponse<Object>> handleUserDomainErrors(
            EmailAlreadyExistException ex) {

        return ResponseEntity.status(CONFLICT).body(CommonResponse.of(ex.getErrorCode()));
    }
}



