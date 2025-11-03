package com.example.commerce_back_office.jwt;

import com.example.commerce_back_office.domain.UserRole;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.example.commerce_back_office.jwt.JwtConst.*;

/**
 * JwtUtil 클래스
 * JWT 토큰을 생성,및 검증 토큰 자체를 다루는 클래스
 */
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    /**
     * 생성자
     * @param secret : application.property 에  spring.jwt.secret 값이 있는경우 이것을 평문으로 사용, 없는경우, 파라메터로 받은 문자열을 사용
     *               이 평문으로 HMAC 알고리즘으로 암호화 한 후에, 이것을 secretKey 로 가지고 있는다
     */
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                MAC_ALGORITHM.key().build().getAlgorithm());
    }

    /**
     * Jwt Signature 의 위조 + 만료 검증
     */
    public Claims getClaims(String token) {//정보를 찾아오려면 시큐리티 키값이 필요함
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
    /**
     * Jwt 토큰을 만들어서 반환하는 함수
     */
    public String createJwt(String category, String email, UserRole role, Long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_CATEGORY, category) // (Access / Refresh)
                .claim(CLAIM_EMAIL, email) //표준 클레임
                .claim(CLAIM_ROLE,role) //payload 에 key - value 형태로 들어간다
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey) //signature 만들기
                .compact();
    }

}
