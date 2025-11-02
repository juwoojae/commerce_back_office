package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.UserDetailResponseDto;
import com.example.commerce_back_office.dto.UserResponseDto;
import com.example.commerce_back_office.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 유저 정보를 조회하는 REST 컨트롤러입니다.
 *
 * GET: 전체 유저 목록 조회
 * GET: 특정 유저 상세 정보 조회
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    /**
     * 전체 유저 목록을 조회합니다.
     *
     * @return List<UserResponseDto>: 전체 유저 정보를 담은 리스트와 HTTP 상태 코드 200
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * 특정 유저의 상세 정보를 조회합니다.
     *
     * @param id 조회할 유저의 ID
     * @return UserDetailResponseDto 해당 유저의 상세 정보와 HTTP 상태 코드 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> getUsers(@PathVariable Long id){

        UserDetailResponseDto users = userService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

  }
