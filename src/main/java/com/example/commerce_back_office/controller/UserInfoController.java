package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.user.UserDetailResponseDto;
import com.example.commerce_back_office.dto.user.UserRequestDto;
import com.example.commerce_back_office.dto.user.UserResponseDto;
import com.example.commerce_back_office.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 유저 정보를 조회하는 REST 컨트롤러입니다.
 * GET: 전체 유저 목록 조회
 * GET: 특정 유저 상세 정보 조회
 * PATCH: 특정 유저의 정보 부분 수정
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
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> getUsers(@PathVariable Long id) {

        UserDetailResponseDto users = userService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * 검색 키워드를 기반으로 일치하는 유저 목록을 조회합니다.
     *
     * @param keyword 유저(이름, 이메일 포함 문자)를 검색할 키워드
     * @return List<UserResponseDto> 검색 조건에 일치하는 유저 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> getAllUsersByKeyword(@RequestParam String keyword) {
        List<UserResponseDto> response = userService.getAllByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 유저의 정보를 부분적으로 수정합니다.
     * JSON 형식의 body를 통해 수정할 필드만 전달합니다.
     *
     * @param id      수정할 유저의 ID
     * @param request 수정할 정보를 담은 요청 DTO
     * @return 수정된 유저 정보 (UserDetailResponseDto)
     */
    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> patchUsers(@PathVariable Long id,@Valid @RequestBody UserRequestDto request) {
        UserDetailResponseDto users = userService.patch(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
