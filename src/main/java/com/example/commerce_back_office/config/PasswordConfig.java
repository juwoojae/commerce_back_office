package com.example.commerce_back_office.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    /**
     * BCryptPasswordEncoder는 문자열 단방향 해시 알고리즘(BCrypt)으로 암호화하여 저장한다
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //해시함수 (비밀번호를 암호화하는)
    }
}