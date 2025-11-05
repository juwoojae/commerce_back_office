package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 정보를 조회하는 REST 컨트롤러입니다.
 * POST: 리뷰 생성
 * GET: 전체 리뷰 목록 조회
 * GET: 특정 리뷰 상세 정보 조회
 */
@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable int productId,
            @RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.save(userPrincipal.getUser(), productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long id) {
        ReviewResponseDto response = reviewService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReview() {
        List<ReviewResponseDto> response = reviewService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> patchReview(@PathVariable Long id, @RequestBody ReviewRequestDto request) {

        ReviewResponseDto response = reviewService.patch(id,request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
