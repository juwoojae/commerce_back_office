package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.Refresh;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.auth.JoinByAdminRequestDto;
import com.example.commerce_back_office.dto.auth.JoinByAdminResponseDto;
import com.example.commerce_back_office.dto.auth.JoinRequestDto;
import com.example.commerce_back_office.dto.auth.JoinResponseDto;
import com.example.commerce_back_office.exception.EmailAlreadyExistException;
import com.example.commerce_back_office.exception.ExpiredException;
import com.example.commerce_back_office.exception.InvalidTokenException;
import com.example.commerce_back_office.exception.TokenMissingException;
import com.example.commerce_back_office.jwt.JwtUtil;
import com.example.commerce_back_office.repository.RefreshRepository;
import com.example.commerce_back_office.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.example.commerce_back_office.jwt.JwtConst.*;

@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;

    /**
     * 회원가입 기능 구현
     * 이렇게 회원가입 할경우 기본적으로 고객권한이 부여
     * 이메일 중복 가입 방지 및 형식 검증
     * @param joinRequestDto
     * @return joinResponseDto
     */

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequestDto) {
        log.info("joinRequestDto {}", joinRequestDto);
        String email = joinRequestDto.getEmail();
        String password = joinRequestDto.getPassword();
        String name = joinRequestDto.getName();
        UserRole role = UserRole.CONSUMER; //일반 회원가입에서는 default 가 CONSUMER

        checkDuplicateEmail(email);
        User data = new User(name, email, passwordEncoder.encode(password), role);
        userRepository.save(data);

        return JoinResponseDto.from(data);
    }
    /**
     * 관리자 회원가입 기능 구현
     * 관리자 회원가입의 경우 UserRole 을 받아온것으로 권한이 부여
     * 이메일 중복 가입 방지 및 형식 검증
     * @param joinByAdminRequestDto
     * @return joinResponseDto
     */

    @Transactional
    public JoinByAdminResponseDto joinByAdmin(JoinByAdminRequestDto joinByAdminRequestDto) {
        log.info("joinByAdminRequestDto {}", joinByAdminRequestDto);
        String email = joinByAdminRequestDto.getEmail();
        String password = joinByAdminRequestDto.getPassword();
        String name = joinByAdminRequestDto.getName();
        UserRole role = joinByAdminRequestDto.getRole(); //일반 회원가입에서는 default 가 CONSUMER

        //중복가입 방지
        checkDuplicateEmail(email);
        User data = new User(name, email, passwordEncoder.encode(password), role);
        userRepository.save(data);

        return JoinByAdminResponseDto.from(data);
    }


    public void reissueToken(String refreshToken) {

        if (refreshToken == null) {
            log.error("토큰이 유실되었음");
            throw new TokenMissingException("토큰 유실");   //403
        }

        Boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);

        if (!isExist) {
            throw new InvalidTokenException("사용할수 없는 토큰");
        }

        Claims claims = null;

        try {
            claims =  jwtUtil.getClaims(refreshToken); //토큰만료 + 위조 + 손상 검증
        } catch (ExpiredJwtException e) {
            log.error("토큰이 이미 만료됨");
            throw new ExpiredException("토큰이 만료되었음");  //토큰 만료 401
        } catch (JwtException e) {
            log.error("사용할수 없는 토큰");
            throw new InvalidTokenException("사용할수 없는 토큰");  //토큰이 위조/손상 401
        }

        if (!jwtUtil.getClaims(refreshToken).get(CLAIM_CATEGORY, String.class)
                .equals(CATEGORY_REFRESH)) {
            throw new InvalidTokenException("사용할수 없는 토큰");  //401
        }


        //유저 정보
        String email = jwtUtil.getClaims(refreshToken).get(CLAIM_EMAIL, String.class);
        UserRole role = jwtUtil.getClaims(refreshToken).get(CLAIM_ROLE, UserRole.class);
        //토큰 생성
        String newAccessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, role, ACCESSION_TIME);//refresh 토큰 생성
        //refresh Rotate
        String newRefreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, role, REFRESH_TOKEN_TIME);

        refreshRepository.existsByRefreshToken(refreshToken); //DB 의 refresh 토큰 삭제
        addRefreshEntity(email, newRefreshToken, REFRESH_TOKEN_TIME);
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh newRefresh = new Refresh(refresh, date.toString(), email);
        refreshRepository.save(newRefresh);
    }


    private void checkDuplicateEmail(String email) {
        //중복가입 방지
        Boolean isExist = userRepository.existsByEmail(email);

        //이메일 중복 가입 검증
        if (isExist) {
            throw new EmailAlreadyExistException("이미 존재하는 이메일");
        }
    }

}
