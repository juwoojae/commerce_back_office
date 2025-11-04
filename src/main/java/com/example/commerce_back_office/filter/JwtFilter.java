package com.example.commerce_back_office.filter;

import com.example.commerce_back_office.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.commerce_back_office.jwt.JwtConst.*;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = request.getHeader(ACCESS_HEADER);

        Claims claims = null;

        if(StringUtils.hasText(tokenValue)) {
            String token = tokenValue.substring(7);
            claims = jwtUtil.validationAndgetClaims(token);
            String email = claims.get(CLAIM_EMAIL, String.class);

            setAuthentication(email);
        }
        filterChain.doFilter(request, response);

    }

    /**
     * 인증 객체를 SecurityContextHolder 에 Authentication 를 저장 하므로서 하나의 요청에 대해서 일시적으로 세션이 생김
     * @param username
     */
    public void setAuthentication (String username){
        log.info("JWT 인증 성공");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username); //유저 정보로 인증 객체 생성

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context); //현재 요청을 인증된 상태로 설정
    }

    /**
     * 인증 객체 Authentication 생성
     * @param username
     * @return
     */
    private Authentication createAuthentication (String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); //username 을 DB 에서 찾아 올때
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());  //인가
    }
}

