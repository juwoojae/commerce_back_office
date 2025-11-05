package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.dto.UserResponseDto;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="reviews")
@NoArgsConstructor
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rating;

    private String content;

    public Review(User user, Product product, Integer rating, String content) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.content = content;
    }
}
