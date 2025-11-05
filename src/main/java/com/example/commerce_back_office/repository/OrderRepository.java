package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //사용자가 생성한 주문 목록 조회
    List<Order> findByUserId(Long userId);
}

