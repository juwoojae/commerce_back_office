package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.repository.ProductRepository;
import com.example.commerce_back_office.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDto save(User user, Long productId, ReviewRequestDto request) {

        //상품 검사
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("product not found: " + productId)
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

    public ReviewResponseDto getOne(Long reviewId,User user) {
        Review review = reviewRepository.findByIdAndUser(reviewId,user).orElseThrow(
                ()-> new IllegalArgumentException("리뷰를 찾을 수 없습니다.")
        );

        return ReviewResponseDto.from(review);

    }

    public List<ReviewResponseDto> getAll(User user) {
        List<Review> reviews = reviewRepository.findAllByUser(user);

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto patch(Long reviewId,User user, ReviewRequestDto request) {
        Review review = reviewRepository.findByIdAndUser(reviewId,user).orElseThrow(
                ()-> new IllegalArgumentException("리뷰를 찾을 수 없습니다.")
        );

        review.patch(request);

        return ReviewResponseDto.from(review);
    }

    public void delete(Long reviewId,User user) {
        //id 검사
        Review review = reviewRepository.findByIdAndUser(reviewId,user).orElseThrow(
                () -> new IllegalStateException("리뷰를 찾을 수 없습니다.")
        );

        //리뷰 삭제
        reviewRepository.deleteById(reviewId);
    }
}
