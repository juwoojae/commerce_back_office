package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.dto.order.OrderCreateRequest;
import com.example.commerce_back_office.dto.order.OrderResponse;
import com.example.commerce_back_office.dto.order.OrderUpdateRequest;
import com.example.commerce_back_office.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderCreateRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    // 2. 주문 리스트 조회
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin) {
        List<OrderResponse> orders = orderService.getOrders(userId, admin);
        return ResponseEntity.ok(orders);
    }

    // 3. 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin) {
        OrderResponse order = orderService.getOrder(orderId, userId, admin);
        return ResponseEntity.ok(order);
    }

    // 4. 주문 수정
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin,
            @RequestBody OrderUpdateRequest request) {
        OrderResponse order = orderService.updateOrder(orderId, userId, admin, request);
        return ResponseEntity.ok(order);
    }

    // 5. 주문 상태 업데이트
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean admin,
            @RequestParam OrderStatus status
    ) {
        OrderResponse order = orderService.updateOrderStatus(orderId, status, userId, admin);
        return ResponseEntity.ok(order);
    }
}





