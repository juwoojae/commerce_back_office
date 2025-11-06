package com.example.commerce_back_office.dto.order;

import com.example.commerce_back_office.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
