package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.order.*;
import com.example.commerce_back_office.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(orderService.createOrder(request, userDetails));
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        return ResponseEntity.ok(orderService.getOrders(userDetails));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 유저 정보
        User user = userDetails.getUser();
        // 로그인 유저 권한
        UserRole role = user.getRole();


        OrderResponse orderResponse = orderService.getOrder(orderId, user, role);
        return ResponseEntity.ok(orderResponse);
    }


    // 주문 수정
    @PatchMapping ("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        return ResponseEntity.ok(orderService.updateOrder(orderId, request, userDetails));
    }

    // 주문 상태 업데이트
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus(), userDetails));
    }
}


