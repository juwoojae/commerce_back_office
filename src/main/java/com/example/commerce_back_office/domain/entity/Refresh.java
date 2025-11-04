package com.example.commerce_back_office.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 블랙 리스팅을 위해서
 * 서버에 refresh 토큰 세션 유지
 */
@Entity
@Getter
@NoArgsConstructor
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email ;  //누구의 토큰인가
    private String refreshToken;  //토큰
    private String refreshTokenExpiration; //만료되는 시간

    public Refresh(String refreshToken, String refreshTokenExpiration, String email) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.email = email;
    }
}
