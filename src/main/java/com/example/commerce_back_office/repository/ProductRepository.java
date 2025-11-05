package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.Category;
import com.example.commerce_back_office.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //  전체 상품 조회
    List<Product> findAll();

    // 이름 또는 카테고리 기준 검색
    List<Product> findByNameContainingIgnoreCaseOrCategory(String name, Category category);
}






