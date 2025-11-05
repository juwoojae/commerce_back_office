package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.domain.entity.User;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.exception.NotFoundException;
import com.example.commerce_back_office.exception.code.ErrorCode;
import com.example.commerce_back_office.repository.ProductRepository;
import com.example.commerce_back_office.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.commerce_back_office.exception.code.ErrorCode.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDto save(User user, Long productId, ReviewRequestDto request) {

        //상품 검사
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(REVIEW_NOT_FOUND)
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

    public ReviewResponseDto getOne(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(REVIEW_NOT_FOUND)
        );

        return ReviewResponseDto.from(review);

    }

    public List<ReviewResponseDto> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto patch(Long id, ReviewRequestDto request) {
        Review review = reviewRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(REVIEW_NOT_FOUND)
        );

        review.patch(request);

        return ReviewResponseDto.from(review);
    }

    public void delete(Long id) {
        //id 검사
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NotFoundException(REVIEW_NOT_FOUND)
        );

        //리뷰 삭제
        reviewRepository.deleteById(id);
    }

    public List<ReviewResponseDto> searchKeword(String keyword) {
        //리뷰 검색
        List<Review> reviews = reviewRepository.searchKeyword(keyword);

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }
}
