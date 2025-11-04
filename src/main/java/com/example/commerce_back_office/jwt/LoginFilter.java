package com.example.commerce_back_office.jwt;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.Refresh;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.auth.LoginRequestDto;
import com.example.commerce_back_office.dto.auth.RefreshResponseDto;
import com.example.commerce_back_office.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static com.example.commerce_back_office.jwt.JwtConst.*;

/**
 * UsernamePasswordAuthenticationFilter 를 커스텀 한 필터
 * login 엔드 포인트를 /user/auth 로 설정
 */
@Slf4j(topic = "로그인 필터")
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RefreshRepository refreshRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtWriter jwtWriter;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,  JwtWriter jwtWriter,RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtWriter = jwtWriter;
        this.refreshRepository = refreshRepository;
        setFilterProcessesUrl("/user/auth");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error("로그인 email, password 파싱 에러 ({})",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword(),
                        null
                )
        );
    }

    /**
     * authenticationManager 에의한 로그인 검증이 되었을때 실행되는 메서드
     * Authentication 에서 세션 정보를 가지고 온뒤, refresh 토큰, Access 토큰 헤더에 포함 시키기
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("성공적으로 로그인 인증");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        //유저 정보
        String email = customUserDetails.getUsername();
        UserRole role = customUserDetails.getUser().getRole();
        //토큰 생성
        String accessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, role, ACCESSION_TIME);
        String refreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, role, REFRESH_TOKEN_TIME);

        jwtWriter.addJwtToHeader(response, accessToken); //Access 토큰 Http헤더에 넣기
        jwtWriter.addJwtToCookie(response, refreshToken,REFRESH_TOKEN_TIME); //Refresh 토큰 쿠키에 넣기

        addRefreshEntity(email,refreshToken,REFRESH_TOKEN_TIME);
    }

    //authenticationManger 의 검증이 실패했으면 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 인증 실패");
        response.setStatus(401);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        refreshRepository.save(new Refresh(refresh, date.toString(), username));
    }
}
