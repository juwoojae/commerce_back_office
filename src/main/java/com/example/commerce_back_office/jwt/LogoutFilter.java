package com.example.commerce_back_office.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

import static com.example.commerce_back_office.jwt.JwtConst.*;

@Slf4j(topic = "logoutFilter")
@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final JwtWriter jwtWriter;



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("CustomLogoutFilter 실행");
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

        String refresh = getCookie(request);

        if (refresh == null) {
            log.error("refresh 토큰이 비어있음");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Claims claims  = jwtUtil.validationAndgetClaims(refresh);


        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = claims.get(CLAIM_CATEGORY, String.class);
        if (!category.equals(REFRESH_HEADER)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //Refresh 토큰 Cookie 값 0
        jwtWriter.addJwtToCookie(response,null,0L);
    }

    private static String getCookie(HttpServletRequest request) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }
        return refresh;
    }
}
