package com.example.commerce_back_office.dto.order;

import lombok.Getter;

@Getter
public class OrderCreateRequest {

    private Long userId;  //유저정보
    private Long productId;  //상품정보
    private Integer quantity; //상품 수량
}
