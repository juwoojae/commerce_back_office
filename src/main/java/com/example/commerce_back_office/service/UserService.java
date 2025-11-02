package com.example.commerce_back_office.service;

import com.example.commerce_back_office.dto.UserDetailResponseDto;
import com.example.commerce_back_office.dto.UserResponseDto;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 유저 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 모든 유저를 조회하고 UserResponseDto 리스트로 반환합니다.
     *
     * @return List<UserResponseDto> 전체 유저 정보를 담은 DTO 리스트
     */
    public List<UserResponseDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID를 가진 유저를 조회하고 유저의 상세 정보를 반환합니다.
     *
     * @param id 조회할 유저의 ID
     * @return UserDetailResponseDto 해당 유저의 상세 정보 DTO
     * @throws IllegalArgumentException 해당 ID의 유저가 존재하지 않을 경우 발생
     */
    public UserDetailResponseDto getOne(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("User not found: " + id)
        );

        return UserDetailResponseDto.from(user);
    }
}
