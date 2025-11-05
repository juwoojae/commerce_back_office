package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.domain.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="products") // DB 테이블명 지정
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
public class Product extends BaseEntity { // BaseEntity 상속: createdDate, lastModifiedDate 포함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    private Long id;

    private String name; // 상품명

    private Integer price; // 상품 가격

    private Integer stock; // 재고 수량

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 DB 저장
    private Category category; // 상품 카테고리 (TOP, BOTTOM, OUTER)

    private String description; // 상품 상세 설명

    /**
     * 상품 생성 시 사용하는 생성자
     */
    public Product(String name, Integer price, Integer stock, Category category, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    // Setter 메서드: 수정 기능에서 사용

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
