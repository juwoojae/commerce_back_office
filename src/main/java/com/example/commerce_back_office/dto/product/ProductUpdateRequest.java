package com.example.commerce_back_office.dto.product;

import com.example.commerce_back_office.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    // 수정할 상품명 (Optional: null이면 수정 안함)
    private String name;

    // 수정할 상품 가격 (Optional: null이면 수정 안함)
    private Integer price;

    // 수정할 상품 재고 수량 (Optional: null이면 수정 안함)
    private Integer stock;

    // 수정할 상품 카테고리 (Optional: null이면 수정 안함)
    private Category category;

    // 수정할 상품 설명 (Optional: null이면 수정 안함)
    private String description;
    // 요청하는 유저 Id
    private Long userId;
}


