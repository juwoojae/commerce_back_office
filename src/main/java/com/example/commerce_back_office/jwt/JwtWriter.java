package com.example.commerce_back_office.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.example.commerce_back_office.jwt.JwtConst.*;

/**
 * http web 서블릿에 종속하여 Jwt JWT 토큰을 HTTP 요청/응답에 적용하는 클래스.
 */
@Slf4j
@Component
public class JwtWriter {
    /**
     * JWT 를 HTTP 헤더에 추가하기
     */
    public void addJwtToHeader(HttpServletResponse response, String token) {
        response.addHeader(ACCESS_HEADER, BEARER_PREFIX + token);
    }

    /**
     * JWT 를 Cookie 에 저장하기
     */
    public void addJwtToCookie(HttpServletResponse response, String token) {
        // RefreshToken 생성
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_HEADER, token)
                .httpOnly(true)                     // XSS 공격 차단
                .secure(true)                       // HTTPS 환경 필수
                .path("/")                          // 쿠키 적용 경로
                .maxAge(REFRESH_TOKEN_TIME)         // 만료 시간 (초 단위)
                .sameSite("Strict")                  // CSRF 공격 방지
                .build();

        // Response 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}
