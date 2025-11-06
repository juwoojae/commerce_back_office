package com.example.commerce_back_office.filter;


import com.example.commerce_back_office.exception.auth.InvalidTokenException;
import com.example.commerce_back_office.jwt.JwtUtil;
import com.example.commerce_back_office.jwt.JwtWebManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

import static com.example.commerce_back_office.exception.code.ErrorCode.TOKEN_INVALID;
import static com.example.commerce_back_office.jwt.JwtConst.*;

/**
 * 로그 아웃 필터
 */
@Slf4j(topic = "logoutFilter")
@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final JwtWebManager jwtWebManager;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/user/logout")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = getRefreshTokenFromCookies(request);

        Claims claims  = jwtUtil.validationAndgetClaims(refresh);


        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = claims.get(CLAIM_CATEGORY, String.class);
        if (!category.equals(REFRESH_HEADER)) {
            log.info("이 토큰은 사용할수 없음");
            throw new InvalidTokenException(TOKEN_INVALID);
        }

        //Refresh 토큰 Cookie 값 0
        jwtWebManager.addJwtToCookie(response,null,0L);
    }

    /**
     * 쿠키에서 refresh 토큰을 가지고 오는 헬퍼메서드
     */
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        String refreshToken = null;  //이것도 JwtUtil 에 넣기
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(REFRESH_HEADER)) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }
}
