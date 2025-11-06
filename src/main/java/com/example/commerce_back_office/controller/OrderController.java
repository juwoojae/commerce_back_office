package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.dto.CommonResponse;
import com.example.commerce_back_office.dto.order.OrderCreateRequest;
import com.example.commerce_back_office.dto.order.OrderResponse;
import com.example.commerce_back_office.dto.order.OrderUpdateRequest;
import com.example.commerce_back_office.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.commerce_back_office.exception.code.SuccessCode.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. 주문 생성
    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(
            @RequestBody OrderCreateRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(CREATED).body(CommonResponse.of(CREATE_ORDER, order));
    }

    // 2. 주문 리스트 조회
    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrders(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin) {
        List<OrderResponse> orders = orderService.getOrders(userId, admin);
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_ORDERS, orders));
    }

    // 3. 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> getOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin) {
        OrderResponse order = orderService.getOrder(orderId, userId, admin);
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_ORDER, order));
    }

    // 4. 주문 수정
    @PatchMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin,
            @RequestBody OrderUpdateRequest request) {
        OrderResponse order = orderService.updateOrder(orderId, userId, admin, request);
        return ResponseEntity.status(OK).body(CommonResponse.of(UPDATE_ORDER, order));
    }

    // 5. 주문 상태 업데이트
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin,
            @RequestParam OrderStatus status
    ) {
        OrderResponse order = orderService.updateOrderStatus(orderId, status, userId, admin);
        return ResponseEntity.status(OK).body(CommonResponse.of(UPDATE_ORDER_STATUS, order));
    }
}






