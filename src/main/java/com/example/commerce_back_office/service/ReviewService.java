package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.UserDetailResponseDto;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.repository.ProductRepository;
import com.example.commerce_back_office.repository.ReviewRepository;
import com.example.commerce_back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDto save(User user, int productId, ReviewRequestDto request) {

        //상품 검사
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("User not found: " + productId)
        );

        Review review = new Review(
            user,
            product,
            request.getRating(),
            request.getContent()
        );

        reviewRepository.save(review);

        return ReviewResponseDto.from(review);
    }
}
