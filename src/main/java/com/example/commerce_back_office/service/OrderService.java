package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.Order;
import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.order.*;
import com.example.commerce_back_office.repository.OrderRepository;
import com.example.commerce_back_office.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 1 주문 생성 (유저, 관리자)
    public OrderResponse createOrder(OrderCreateRequest request, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        Integer totalPrice = product.getPrice() * request.getQuantity();

        Order order = new Order(user, product, OrderStatus.ORDERED, totalPrice, request.getQuantity());
        orderRepository.save(order);

        return new OrderResponse(order);
    }

    // 2 주문 목록 조회
    public List<OrderResponse> getOrders(CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        List<Order> orders = (role == UserRole.ADMIN) ?
                orderRepository.findAll() :
                orderRepository.findByUserId(userId);

        return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    // 3 특정 주문 조회
    public OrderResponse getOrder(Long orderId, User user,UserRole role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Long currentUserId = user.getId();          // 로그인한 유저
        Long orderUserId = order.getUser().getId(); //주문에 등록된 유저

        //일반 유저가 다른 유저 주문시 권한 없음
        if (role != UserRole.ADMIN && !order.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        return new OrderResponse(order);
    }

    // 4 주문 수정 (유저, 관리자)
    public OrderResponse updateOrder(Long orderId, OrderUpdateRequest request, CustomUserDetails userDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        if (role != UserRole.ADMIN && !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        //상품 변경
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
            order.setProduct(product);
            // totalPrice도 새 상품 가격 반영
            order.setTotalPrice(product.getPrice() * (request.getQuantity() != null ? request.getQuantity() : order.getQuantity()));
        }

        //수량 변경
        if (request.getQuantity() != null) {
            order.setQuantity(request.getQuantity());
            order.setTotalPrice(order.getProduct().getPrice() * order.getQuantity());
        }

        return new OrderResponse(order);
    }

    // 5 주문 상태 업데이트
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, CustomUserDetails userDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Long userId = userDetails.getUser().getId();
        UserRole role = userDetails.getUser().getRole();

        // 주문 취소: 유저 자기 주문 + 관리자
        if (status == OrderStatus.CANCELLED) {
            if (role != UserRole.ADMIN && !order.getUser().getId().equals(userId)) {
                throw new RuntimeException("권한이 없습니다.");
            }
        }
        // 배송중, 배송완료관리자만
        else if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            if (role != UserRole.ADMIN) {
                throw new RuntimeException("관리자 권한이 필요합니다.");
            }
        }

        order.updateStatus(status);
        return new OrderResponse(order);
    }
}

