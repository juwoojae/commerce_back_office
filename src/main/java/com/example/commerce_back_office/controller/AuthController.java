package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.auth.JoinRequestDto;
import com.example.commerce_back_office.dto.auth.JoinResponseDto;
import com.example.commerce_back_office.jwt.JwtUtil;
import com.example.commerce_back_office.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    /**
     * 회원 가입처리 컨트롤러
     */
    @RequestMapping("/user/register")
    public ResponseEntity<JoinResponseDto> joinProcess(@RequestBody JoinRequestDto joinRequestDto) {
        log.info("joinProcess {} {}", joinRequestDto.getEmail(), joinRequestDto.getPassword());
        JoinResponseDto result = authService.join(joinRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
