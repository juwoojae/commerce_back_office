package com.example.commerce_back_office.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.commerce_back_office.domain.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이름 또는 이메일에서 대소문자 구분 없이 특정 문자를 포함한 데이터 찾아서 반환
    List<User> findByNameIgnoreCaseContainingOrEmailIgnoreCaseContaining(String name, String email);

    // 유저 리스트 검색
    default List<User> searchKeyword(String keyword) {
        return findByNameIgnoreCaseContainingOrEmailIgnoreCaseContaining(keyword, keyword);
    }
}
