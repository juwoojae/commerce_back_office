package com.example.commerce_back_office.dto.product;

import com.example.commerce_back_office.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductDetailResponse {

    // 상품 id
    private Long id;

    // 상품명
    private String name;

    // 상품 설명
    private String description;

    // 가격
    private Integer price;

    // 재고 수량
    private Integer stock;

    // 상품 카테고리
    private Category category;

    // 상품 등록일 (BaseEntity에서 자동 생성)
    private LocalDateTime createdDate;

    // 상품 수정일 (BaseEntity에서 자동 업데이트)
    private LocalDateTime lastModifiedDate;

    // 재고가 5개 이하일 경우 true, 아니면 false
    private boolean lowStock;

    // 재고가 부족할 경우 경고 메시지
    private String warningMessage;
}



