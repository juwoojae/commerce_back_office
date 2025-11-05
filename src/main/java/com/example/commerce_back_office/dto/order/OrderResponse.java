package com.example.commerce_back_office.dto.order;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.domain.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    //주문 iD
    private Long id;
    //주문한 사용자
    private Long userId;
    //주문한 상품
    private Long productId;
    //주문 수량
    private int quantity;
    //주문 상태( 주문완료, 배송중, 배송완료, 주문취소)
    private OrderStatus status;
    //총주문금액
    private Integer totalPrice;
    //주문 상태 메세지
    private String message;
    //주문 생성일
    private LocalDateTime createdDate;

    //ORder 엔티티 받아 DTO로 반환 생성자
    public OrderResponse(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.productId = order.getProduct().getId();
        this.quantity = order.getQuantity();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.createdDate = order.getCreatedDate();

        //주문 상태에 따라 사용자에게 보이는 메세지
        switch (order.getStatus()) {
            case ORDERED -> this.message = "주문이 완료되었습니다.";
            case SHIPPED -> this.message = "주문이 배송중입니다.";
            case DELIVERED -> this.message = "주문이 배송 완료되었습니다.";
            case CANCELLED -> this.message = "주문이 취소되었습니다.";
        }
    }
}



