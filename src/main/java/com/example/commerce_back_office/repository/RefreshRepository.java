package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    //해당 토큰이 존재하는지
    Boolean existsByRefreshToken(String refreshToken);

    //해당 리프레쉬 토큰 세션에서 삭제하기
    @Transactional
    void deleteByRefreshToken(String refresh);

}
