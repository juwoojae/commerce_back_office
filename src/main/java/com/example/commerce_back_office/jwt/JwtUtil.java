package com.example.commerce_back_office.jwt;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.exception.auth.ExpiredException;
import com.example.commerce_back_office.exception.auth.InvalidTokenException;
import com.example.commerce_back_office.exception.auth.TokenMissingException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.example.commerce_back_office.exception.code.ErrorCode.*;
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
    public Claims validationAndgetClaims(String token) {//정보를 찾아오려면 시큐리티 키값이 필요함

        if(token == null){
            log.info("토큰이 유실되었음");
            throw new TokenMissingException(TOKEN_MISSING);
        }
        Claims claims = null;
        try {
            claims =  Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            log.error("토큰이 이미 만료됨");
            throw new ExpiredException(TOKEN_EXPIRED);  //토큰 만료 401
        } catch (JwtException e) {
            log.error("사용할수 없는 토큰");
            throw new InvalidTokenException(TOKEN_INVALID);  //토큰이 위조/손상 401
        }
        return claims;
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
