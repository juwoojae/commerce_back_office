package com.example.commerce_back_office.service;


import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email) //DB 안에 없다
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}