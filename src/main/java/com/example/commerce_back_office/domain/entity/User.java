package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.dto.user.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void patchUser(UserRequestDto requestDto){
        // 이름 수정을 요청했을 때만
        if (requestDto.getName() != null && !requestDto.getName().equals(this.getName()) ) {
            this.name = requestDto.getName();
        }

        // 권한 수정을 요청했을 때만
        if (requestDto.getRole() != null && !requestDto.getRole().equals(this.getRole())) {
            this.role = requestDto.getRole();
        }
    }
}
