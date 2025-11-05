package com.example.commerce_back_office.exception;

import com.example.commerce_back_office.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 전역으로 처리
public class GlobalExceptionHandler {

    // 1️⃣ @Valid 유효성 검사 실패 시 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // 각 필드별 오류 메시지를 Map에 저장
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // 400 Bad Request 응답 반환
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2️⃣ 재고가 0 이하로 입력되는 경우 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now()); // 오류 발생 시각
        body.put("status", 400); // HTTP 상태 코드
        body.put("error", "Bad Request"); // 오류 타입
        body.put("message", e.getMessage()); // 예외 메시지
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 3️⃣ 존재하지 않는 상품 조회 등 RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now()); // 오류 발생 시각
        body.put("status", 404); // HTTP 상태 코드
        body.put("error", "Not Found"); // 오류 타입
        body.put("message", e.getMessage()); // 예외 메시지
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * 도메인 정책 위반 - 회원 등록에서 하나의 이메일을 중복등록 하는경우
     */
    @ExceptionHandler (EmailAlreadyExistException.class)
    public ResponseEntity<CommonResponse<Object>> handleUserDomainErrors(EmailAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CommonResponse.of(ex.getErrorCode()));
    }
}



