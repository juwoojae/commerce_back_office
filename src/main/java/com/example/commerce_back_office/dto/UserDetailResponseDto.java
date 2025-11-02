package com.example.commerce_back_office.dto;

import com.example.commerce_back_office.domain.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
/**
 * 유저 상세 정보를 담는 응답 dto
 *
 * 특정 사용자의 상세 정보를 조회할 때 사용됩니다.</p>
 *
 */
@Getter
public class UserDetailResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    /**
     * UserDetail 응답 객체를 생성합니다.
     *
     * @param id            고유ID
     * @param email         유저 이메일
     * @param name          유저 이름
     * @param role          권한
     * @param created_at    유저 생성일
     * @param updated_at    유저 정보 수정일
     */
    public UserDetailResponseDto(Long id, String email, String name, String role, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    /**
     * User 엔티티 객체를 기반으로 UserDetailResponseDto 객체를 생성합니다.
     *
     * @param user: 변환할 User 엔티티 객체
     * @return UserDetailResponseDto: 유저의 상세 정보를 담은 객체
     */
    public static UserDetailResponseDto from(User user) {
        return new UserDetailResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedDate(),
                user.getLastModifiedDate()
        );
    }
}
