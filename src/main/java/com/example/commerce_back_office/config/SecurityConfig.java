package com.example.commerce_back_office.config;

import com.example.commerce_back_office.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티 필터 통합 등록 클래스
 *
 * 커스텀한 시큐리티의 필터를 등록 및 설정
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true) // ✅ 이거 추가
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtWriter jwtWriter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable JWT 에서는 굳이 방어할 필요가 없다
        http
                .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        //글로벌 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/user/register", "/user/auth").permitAll() //모든 권한에 대해서 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")  //ADMIN 권한에 대해서만 허용
                        .anyRequest().authenticated()); //그 이외 로그인한 사용자에 대해 허용


        //Jwt 검증 필터 추가
        http
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailsService), LoginFilter.class);

        //로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,jwtWriter), UsernamePasswordAuthenticationFilter.class);
        //로그 아웃 필터 추가
        http
                .addFilterAt(new LogoutFilter(jwtUtil, jwtWriter), LoginFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 Stateless 상태로 관리 (JWT)
                );

        return http.build();
    }
}
