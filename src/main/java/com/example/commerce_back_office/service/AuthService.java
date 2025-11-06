package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.Refresh;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.auth.*;
import com.example.commerce_back_office.exception.EmailAlreadyExistException;
import com.example.commerce_back_office.exception.auth.InvalidTokenException;
import com.example.commerce_back_office.jwt.JwtUtil;
import com.example.commerce_back_office.repository.RefreshRepository;
import com.example.commerce_back_office.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.example.commerce_back_office.exception.code.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.example.commerce_back_office.exception.code.ErrorCode.TOKEN_INVALID;
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

    /**
     * refresh 토큰과 Access 토큰을 재발행한뒤
     * dto 로 넣어서 넘겨주는 메서드
     * 그리고 refreshEntity 에서 이전에 사용했던 refresh 토큰을 모두 말소함
     * @param refreshToken
     * @return
     */
    @Transactional
    public RefreshResponseDto reissueToken(String refreshToken) {

        log.info("reissueToken {}", refreshToken);

        Claims claims = jwtUtil.validationAndgetClaims(refreshToken); //토큰만료 + 위조 + 손상 검증

        Boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);

        if (!isExist) {
            log.info("이 토큰은 사용할수 없음");
            throw new InvalidTokenException(TOKEN_INVALID);
        }

        if (!claims.get(CLAIM_CATEGORY, String.class)
                .equals(CATEGORY_REFRESH)) {
            log.info("이 토큰은 사용할수 없음");
            throw new InvalidTokenException(TOKEN_INVALID);  //401
        }
        //유저 정보
        String email = claims.get(CLAIM_EMAIL, String.class);

        String roleString = claims.get(CLAIM_ROLE, String.class);
        UserRole userRole = UserRole.valueOf(roleString);

        //토큰 생성
        String newAccessToken = jwtUtil.createJwt(CATEGORY_ACCESS, email, userRole, ACCESSION_TIME);//refresh 토큰 생성
        //refresh Rotate
        String newRefreshToken = jwtUtil.createJwt(CATEGORY_REFRESH, email, userRole, REFRESH_TOKEN_TIME);

        refreshRepository.deleteByRefreshToken(refreshToken); //DB 의 refresh 토큰 삭제

        addRefreshEntity(email, newRefreshToken, REFRESH_TOKEN_TIME);
        return new RefreshResponseDto(newAccessToken, newRefreshToken);
    }

    /**
     * 해당 refresh 토큰을 서버가 관리하는 refresh 토큰 세션에 추가하기
     * @param email : 회원 id
     * @param refresh : refresh 토큰
     * @param expiredMs : refresh 토큰 만료 시간
     */
    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh newRefresh = new Refresh(refresh, date.toString(), email);
        refreshRepository.save(newRefresh);
    }

    /**
     * 해당 이메일과 같은 이메일이 user db 정보에 존재하는지 판단하기.
     * @param email
     */
    private void checkDuplicateEmail(String email) {
        //중복가입 방지
        Boolean isExist = userRepository.existsByEmail(email);

        //이메일 중복 가입 검증
        if (isExist) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST);
        }
    }

}
