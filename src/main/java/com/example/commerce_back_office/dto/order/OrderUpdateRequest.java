package com.example.commerce_back_office.dto.order;

import com.example.commerce_back_office.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequest {
    private Long productId;        // 상품 변경
    private Integer quantity;      // 수량 변경
}

