package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.auth.JoinRequestDto;
import com.example.commerce_back_office.dto.auth.JoinResponseDto;
import com.example.commerce_back_office.exception.EmailAlreadyExistException;
import com.example.commerce_back_office.jwt.JwtUtil;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

        //중복가입 방지
        Boolean isExist = userRepository.existsByEmail(email);

        //이메일 중복 가입 검증
        if (isExist) {
            throw new EmailAlreadyExistException("이미 존재하는 이메일");
        }
        User data = new User(name, email, passwordEncoder.encode(password), role);
        userRepository.save(data);

        return JoinResponseDto.from(data);
    }
}
