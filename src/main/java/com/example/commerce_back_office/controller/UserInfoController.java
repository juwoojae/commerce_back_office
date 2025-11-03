package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.UserDetailResponseDto;
import com.example.commerce_back_office.dto.UserRequestDto;
import com.example.commerce_back_office.dto.UserResponseDto;
import com.example.commerce_back_office.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> getUsers(@PathVariable Long id) {

        //TODO 관리자만 조회 가능
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
    @PatchMapping("/{id}")
    public ResponseEntity<UserDetailResponseDto> patchUsers(@PathVariable Long id, @RequestBody UserRequestDto request) {
        //TODO - 권한이 없는 사용자의 요청은 `403 Forbidden` 으로 처리
        UserDetailResponseDto users = userService.patch(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
