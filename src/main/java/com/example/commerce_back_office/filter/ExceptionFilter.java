package com.example.commerce_back_office.filter;

import com.example.commerce_back_office.dto.auth.AuthErrorResponseDto;
import com.example.commerce_back_office.exception.ExpiredException;
import com.example.commerce_back_office.exception.InvalidTokenException;
import com.example.commerce_back_office.exception.JsonParsingFailedException;
import com.example.commerce_back_office.exception.TokenMissingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 인증, 인가 톰합 예외 처리 필터
 *
 * ExceptionHandler 로 처리하지 못하는 예외에 대한 통합 처리
 * 시큐리티 필터단에서 발생할수 있는 커스텀 예외 처리
 *
 * 1. TokenMissingException 토큰 유실 예외
 * 2. ExpiredException 토큰 만료 예외
 * 3. InvalidTokenException 토큰의 검증 실패 예외
 * 4. JsonParsingFailedException Json 파싱 예외 처리
 */
@Slf4j(topic = "ExceptionFilter")
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (TokenMissingException ex) { // 토큰 아예 없음 → 403로 요청 차단
            log.error("토큰 유실 에러 : {} path={}", ex.getMessage(), request.getRequestURI());
            writeErrorResponse(response, HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());

        } catch (ExpiredException ex) { // 만료 → 401
            log.error("토큰 만료 에러 : {} path={}", ex.getMessage(), request.getRequestURI());
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());

        } catch (InvalidTokenException ex) { // 잘못된 토큰 → 401
            log.error("토큰 검증 에러 : {} path={}", ex.getMessage(), request.getRequestURI());
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());

        } catch (JsonParsingFailedException ex) { // JSON 자체가 문제 → 400
            log.error("로그인 데이터 파싱 에러 : {} path={}", ex.getMessage(), request.getRequestURI());
            writeErrorResponse(response, HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        }
    }

    private void writeErrorResponse(HttpServletResponse response,
                                    HttpStatus status,
                                    String message,
                                    String path) throws IOException {

        AuthErrorResponseDto err = new AuthErrorResponseDto(status, message, path);
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        String body = objectMapper.writeValueAsString(err);
        response.getWriter().write(body);
        response.getWriter().flush();
    }

}
