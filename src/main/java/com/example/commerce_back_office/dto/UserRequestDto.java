package com.example.commerce_back_office.dto;

import com.example.commerce_back_office.domain.UserRole;
import jakarta.annotation.Nullable;
import lombok.Getter;

/**
 * 유저 정보 수정 요청 DTO
 * PATCH 요청 시 클라이언트로부터 전달되는 유저 정보입니다.
 * name과 role 필드는 선택적(@Nullable)이며, 전달되지 않은 필드는 수정되지 않습니다.
 */
@Getter
public class UserRequestDto {

    @Nullable
    private final String name;
    @Nullable
    private final UserRole role;

    /**
     * UserRequestDto 응답 객체를 생성합니다.
     * @param name          유저 이름
     * @param role          권한
     */
    public UserRequestDto(String name, UserRole role) {
        this.name = name;
        this.role = role;
    }
}
