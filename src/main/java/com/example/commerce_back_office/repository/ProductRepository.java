package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.Category;
import com.example.commerce_back_office.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1 전체 상품 조회
    // JpaRepository에서 기본 제공하는 findAll() 메서드
    // 전체 상품 리스트를 가져올 때 사용
    List<Product> findAll();

    // 2 이름 또는 카테고리 기준 검색
    // 상품 이름에 키워드가 포함되거나, 카테고리가 일치하는 상품 목록 조회
    List<Product> findByNameContainingIgnoreCaseOrCategory(String name, Category category);
}






