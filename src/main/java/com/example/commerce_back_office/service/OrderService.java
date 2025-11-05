package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.OrderStatus;
import com.example.commerce_back_office.domain.entity.Order;
import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.order.OrderCreateRequest;
import com.example.commerce_back_office.dto.order.OrderResponse;
import com.example.commerce_back_office.dto.order.OrderUpdateRequest;
import com.example.commerce_back_office.repository.OrderRepository;
import com.example.commerce_back_office.repository.ProductRepository;
import com.example.commerce_back_office.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    //주문 생성
    public OrderResponse createOrder(OrderCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        int totalPrice = product.getPrice() * request.getQuantity();

        Order order = new Order(user, product, OrderStatus.ORDERED, totalPrice, request.getQuantity());
        orderRepository.save(order);

        // 상태 메시지도 포함됨
        return new OrderResponse(order);
    }

    //주문 목록 조회
    public List<OrderResponse> getOrders(Long userId, boolean admin) {
        List<Order> orders;
        if (admin) {
            // 관리자는 모든 주문 조회 가능
            orders = orderRepository.findAll();
        } else {
            // 일반 유저는 자신이 주문한 것만 조회
            orders = orderRepository.findByUserId(userId);
        }

        return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    //특정 주문 조회
    public OrderResponse getOrder(Long orderId, Long userId, boolean admin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        //다른 유저의 주문이면 예외
        if (!admin && !order.getUser().getId().equals(userId))
            throw new RuntimeException("권한이 없습니다.");

        return new OrderResponse(order); // 상태 메시지 포함
    }

    //특정 주문 수정
    public OrderResponse updateOrder(Long orderId, Long userId, boolean admin, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 본인주문만 수정가능
        if (!admin && !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 상품 변경 시
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
            order.setProduct(product);

            // 총 가격 다시 계산
            order.setTotalPrice(product.getPrice() * (request.getQuantity() != null ? request.getQuantity() : order.getQuantity()));
        }

        // 수량 변경
        if (request.getQuantity() != null) {
            order.setQuantity(request.getQuantity());
            order.setTotalPrice(order.getProduct().getPrice() * order.getQuantity());
        }

        return new OrderResponse(order);
    }

    // 주문 상태 업데이트
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Long userId, boolean admin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (status == OrderStatus.CANCELLED) {

            // 주문 취소는 유저 본인 또는 관리자만 가능
            if (!admin && !order.getUser().getId().equals(userId)) {
                throw new RuntimeException("권한이 없습니다.");
            }
        } else {

            // 그 외 상태 변경은 관리자만 가능
            if (!admin) {
                throw new RuntimeException("관리자 권한이 필요합니다.");
            }
        }
        order.updateStatus(status);
        return new OrderResponse(order);
    }
}





