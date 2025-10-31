package com.example.commerce_back_office.domain;

public enum OrderStatus {
    //배송중, 출고, 배송준비중
    SHIPPED,       // 배송 중
    DELIVERED,     // 배송 완료
    CANCELLED,     // 주문 취소
}
