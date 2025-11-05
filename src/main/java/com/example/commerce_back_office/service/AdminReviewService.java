package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponseDto getOne(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("review not found: " + id)
        );

        return ReviewResponseDto.from(review);

    }

    public List<ReviewResponseDto> getAll() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        //id 검사
        reviewRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 id입니다.")
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
