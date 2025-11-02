package com.example.commerce_back_office.service;

import com.example.commerce_back_office.dto.UserResponseDto;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }
}
