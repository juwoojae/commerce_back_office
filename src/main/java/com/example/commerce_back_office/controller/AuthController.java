package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.CommonResponse;
import com.example.commerce_back_office.dto.auth.*;
import com.example.commerce_back_office.jwt.JwtWebManager;
import com.example.commerce_back_office.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.example.commerce_back_office.exception.code.SuccessCode.*;
import static com.example.commerce_back_office.jwt.JwtConst.REFRESH_HEADER;
import static com.example.commerce_back_office.jwt.JwtConst.REFRESH_TOKEN_TIME;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtWebManager jwtWebManager;
    private final AuthService authService;

    /**
     * 회원 가입처리 컨트롤러
     */
    @PostMapping("/user/register")
    public ResponseEntity<CommonResponse<JoinResponseDto>> joinProcess(@Valid @RequestBody JoinRequestDto joinRequestDto) {

        log.info("joinProcess {} {}", joinRequestDto.getEmail(), joinRequestDto.getPassword());
        JoinResponseDto result = authService.join(joinRequestDto);

        return ResponseEntity.status(CREATED).body(CommonResponse.of(JOIN_JOIN_PROCESS_BY_ADMIN, result));
    }

    /**
     * 관리자 에 의한 회원 가입처리 컨트롤러
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/register")
    public ResponseEntity<CommonResponse<JoinByAdminResponseDto>> joinByAdminProcess(
            @Valid @RequestBody JoinByAdminRequestDto joinByAdminRequestDto) {

        log.info("joinByAdminProcess {} {}", joinByAdminRequestDto.getEmail(), joinByAdminRequestDto.getPassword());
        JoinByAdminResponseDto result = authService.joinByAdmin(joinByAdminRequestDto);

        return ResponseEntity.status(CREATED).body(CommonResponse.of(JOIN_PROCESS, result));
    }

    /**
     * refresh 토큰 재발행 컨트롤러
     * 쿠키에서 refreshToken 꺼내기
     * 서비스로직실행후, refreshToken,AccessToken 재발급후
     * refresh 토큰 재발행후 쿠키에 넣기, access 토큰 재발행후 헤더에 넣기.
     */
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<RefreshResponseDto>> refreshProcess(HttpServletRequest request, HttpServletResponse response) {

        log.info("refreshProcess 실행");
        String refreshToken = getRefreshTokenFromCookies(request);
        RefreshResponseDto result = authService.reissueToken(refreshToken);

        jwtWebManager.addJwtToHeader(response, result.getAccessToken());
        jwtWebManager.addJwtToCookie(response, result.getRefreshToken(), REFRESH_TOKEN_TIME);

        return ResponseEntity.status(CREATED).body(CommonResponse.of(REFRESH_PROCESS, result));
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
