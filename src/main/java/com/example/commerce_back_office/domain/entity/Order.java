package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //주문자 정보
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //상품정보
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //주문자 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 총 상품 가격
    private Integer totalPrice;

    //주문 수량
    private Integer quantity;


    //주문생성자
    public Order(User user, Product product, OrderStatus status, Integer totalPrice, Integer quantity) {
        this.user = user;
        this.product = product;
        this.status = status;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    //상품 정보 수정
    public void setProduct(Product product) {
        this.product = product;
    }

    //주문 수량수정
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    //주문 총 금액 수정
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    //주문 상태 업데이트
    public void updateStatus(OrderStatus newstatus){
        this.status = newstatus;
    }
}
