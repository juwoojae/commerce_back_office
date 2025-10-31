package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer totalPrice;

    public Order(User user, OrderStatus status, Integer totalPrice) {
        this.user = user;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
