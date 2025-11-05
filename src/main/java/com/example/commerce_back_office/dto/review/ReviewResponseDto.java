package com.example.commerce_back_office.dto.review;

import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.UserDetailResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ReviewResponseDto {

    private final String username;
    private final String productName;
    private final Integer rating;
    private final String content;
    private final LocalDateTime created_at;

    public ReviewResponseDto(String username, String productName, Integer rating, String content, LocalDateTime created_at) {
        this.username = username;
        this.productName = productName;
        this.rating = rating;
        this.content = content;
        this.created_at = created_at;
    }

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getUser().getName(),
                review.getProduct().getName(),
                review.getRating(),
                review.getContent(),
                review.getCreatedDate()
        );
    }
}
