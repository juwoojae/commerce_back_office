package com.example.commerce_back_office.config;

import com.example.commerce_back_office.jwt.*;
import com.example.commerce_back_office.filter.ExceptionFilter;
import com.example.commerce_back_office.filter.JwtFilter;
import com.example.commerce_back_office.filter.LoginFilter;
import com.example.commerce_back_office.filter.LogoutFilter;
import com.example.commerce_back_office.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static com.example.commerce_back_office.jwt.JwtConst.ACCESS_HEADER;

/**
 * 스프링 시큐리티 필터 통합 등록 클래스
 * <p>
 * 커스텀한 시큐리티의 필터를 등록 및 설정
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtWebManager jwtWebManager;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //CORS 설정
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {

                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request){
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Collections.singletonList(ACCESS_HEADER));

                                return configuration;
                            }
                        }));

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

        //로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, jwtWebManager, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        //Jwt 검증 필터 추가
        http
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailsService), LoginFilter.class);

        //예외처리 필터 추가
        http
                .addFilterBefore(new ExceptionFilter(),JwtFilter.class);


        //로그 아웃 필터 추가
        http
                .addFilterAt(new LogoutFilter(jwtUtil, jwtWebManager), LoginFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 Stateless 상태로 관리 (JWT)
                );

        return http.build();
    }
}
