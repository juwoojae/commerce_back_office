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

    //상품명
    private String name;

    //상품 가격
    private Integer price;

    //재고 수량
    private Integer stock;

    // Enum 타입을 문자열로 DB 저장
    @Enumerated(EnumType.STRING)

    // 상품 카테고리 (TOP, BOTTOM, OUTER)
    private Category category;

    // 상품 상세 설명
    private String description;


    public Product(String name, Integer price, Integer stock, Category category, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    // 상품명 수정
    public void setName(String name) {
        this.name = name;
    }

    //상푸ㅁ가격 수정
    public void setPrice(Integer price) {
        this.price = price;
    }

    //상품 재고 수정
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    //상품 카테고리수정
    public void setCategory(Category category) {
        this.category = category;
    }


    //상품 상세 설명 수정
    public void setDescription(String description) {
        this.description = description;
    }
}
